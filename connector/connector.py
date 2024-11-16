import requests, json, sys, uuid, time
from datetime import datetime
from jsonpath_ng import jsonpath, parse

json_folder="path/to/json/messages/"
timeout_agreement=10 # number of seconds to poll for an agreement identifier
timeout_transfer=10 # number of seconds to poll for an endpoint data reference

### READ API KEY

key_file = open("secret.txt", "r") # Prepare a text file with only the secret api key as content
apikey="".join(key_file.readlines()).strip()
print(f"API KEY: {apikey}")
key_file.close()

### METHODS

def is_valid_uuid(val):
    try:
        uuid.UUID(str(val))
        return True
    except ValueError:
        return False

def try_find_value_in_json(jsonpath_expr, json_data):
    try:
        return jsonpath_expr.find(json_data)[0].value
    except:
        return None
    
def call_endpoint(endpoint, data=None):
    if data:
        response = requests.post(url=endpoint, data=data, headers={"Content-Type": "application/json"})
    else:
        response = requests.get(url=endpoint, headers={"Content-Type": "application/json"})
    response_dict = response.json() 
    print(f"Response:\n{json.dumps(response_dict, indent=4, sort_keys=True)}")
    return response_dict

### MAIN

### PREREQUISITE
### Start consumer: java -Dedc.keystore=connector/resources/certs/cert.pfx -Dedc.keystore.password=123456 -Dedc.fs.config=connector/resources/configuration/consumer-configuration.properties -jar connector/build/libs/connector.jar
### Start provider: java -Dedc.keystore=connector/resources/certs/cert.pfx -Dedc.keystore.password=123456 -Dedc.fs.config=connector/resources/configuration/provider-configuration.properties -jar connector/build/libs/connector.jar

### Read the JSON messages for later POST
create_asset=None
with open(json_folder+'create-asset.json', 'r') as create_asset_file:
    create_asset = json.load(create_asset_file)
create_asset=json.loads(json.dumps(create_asset).replace("{{APIKEY}}", apikey))
print(f"create_asset as json:\n{json.dumps(create_asset,indent=4)}")

create_policy=None
with open(json_folder+'create-policy.json', 'r') as create_policy_file:
    create_policy = json.load(create_policy_file)
print(f"create_policy as json:\n{json.dumps(create_policy,indent=4)}")

create_contract=None
with open(json_folder+'create-contract-definition.json', 'r') as create_contract_file:
    create_contract = json.load(create_contract_file)
print(f"create_contract as json:\n{json.dumps(create_contract,indent=4)}")

fetch_catalog=None
with open(json_folder+'fetch-catalog.json', 'r') as fetch_catalog_file:
    fetch_catalog = json.load(fetch_catalog_file)
print(f"fetch_catalog as json:\n{json.dumps(fetch_catalog,indent=4)}")

negotiate_contract=None
with open(json_folder+'negotiate-contract.json', 'r') as negotiate_contract_file:
    negotiate_contract = json.load(negotiate_contract_file)
print(f"negotiate_contract as json:\n{json.dumps(negotiate_contract,indent=4)}")

start_transfer=None
with open(json_folder+'start-transfer.json', 'r') as start_transfer_file:
    start_transfer = json.load(start_transfer_file)
print(f"start_transfer as json:\n{json.dumps(start_transfer,indent=4)}")

### Execute the workflow

print(f"1. CREATE ASSET")
call_endpoint("http://localhost:19193/management/v3/assets", json.dumps(create_asset))

print(f"2. CREATE POLICY")
call_endpoint("http://localhost:19193/management/v3/policydefinitions", json.dumps(create_policy))

print(f"3. CREATE CONTRACT")
call_endpoint("http://localhost:19193/management/v3/contractdefinitions", json.dumps(create_contract))

print(f"4. FETCH CATALOG")
response_json=call_endpoint("http://localhost:19193/management/v3/catalog/request", json.dumps(fetch_catalog))
jsonpath_expr = parse('$."dcat:dataset"."odrl:hasPolicy"."@id"')
dataset_id=try_find_value_in_json(jsonpath_expr, response_json)
print(f"  Dataset ID: {dataset_id}")
if (not dataset_id or not dataset_id.startswith("MQ")):
    logging.error(f"Found an unexpected kind of dataset identifier: {dataset_id}")
    exit(1)

print(f"5. NEGOTIATE CONTRACT")
negotiate_contract=json.loads(json.dumps(negotiate_contract).replace("{{DATASET_ID}}", dataset_id))
print(f"negotiate_contract as json:\n{json.dumps(negotiate_contract, indent=4, sort_keys=True)}")
response_json = call_endpoint("http://localhost:29193/management/v3/contractnegotiations", json.dumps(negotiate_contract))
jsonpath_expr = parse('$."@id"')
negotiation_id=try_find_value_in_json(jsonpath_expr, response_json)
print(f"  Negotiation ID: {negotiation_id}")
if (not negotiation_id or not is_valid_uuid(negotiation_id)):
    logging.error(f"Found an unexpected kind of negotiation identifier: {negotiation_id}")
    exit(1)

print(f"6. GET AGREEMENT ID")
# poll for the agreement identifier
endtime = time.time() + timeout_agreement
count=0
agreement_id=None
while time.time() < endtime:
    count+=1
    response_json = call_endpoint(f"http://localhost:29193/management/v3/contractnegotiations/{negotiation_id}")
    jsonpath_expr = parse('$.contractAgreementId')
    agreement_id=try_find_value_in_json(jsonpath_expr, response_json)
    print(f"  Agreement ID: {agreement_id}")
    if (not agreement_id or not is_valid_uuid(agreement_id)):
        if (time.time()+1 < endtime):
            print(f"    no agreement identifier found, poll again in a second ({count})")
            time.sleep(1)
        else:
            print(f"  timeout reached")
    else:
        break
if (not agreement_id or not is_valid_uuid(agreement_id)):
    logging.error(f"Found an unexpected kind of agreement identifier: {negotiation_id}")
    exit(1)

print(f"7. START TRANSFER")
start_transfer=json.loads(json.dumps(start_transfer).replace("{{CONTRACT_ID}}", agreement_id))
print(f"start transfer as json:\n{json.dumps(start_transfer, indent=4, sort_keys=True)}")
response_json = call_endpoint("http://localhost:29193/management/v3/transferprocesses", json.dumps(start_transfer))
jsonpath_expr = parse('$."@id"')
transfer_id=try_find_value_in_json(jsonpath_expr, response_json)
print(f"  Transfer ID: {transfer_id}")
if (not transfer_id or not is_valid_uuid(transfer_id)):
    logging.error(f"Found an unexpected kind of transfer identifier: {transfer_id}")
    exit(1)

print(f"8.1. GET ENDPOINT DATA REFERENCE")
# poll for the endpoint data reference
endtime = time.time() + timeout_transfer
count=0
data_endpoint=None
token=None
while time.time() < endtime:
    count+=1
    response_json = call_endpoint(f"http://localhost:29193/management/v3/edrs/{transfer_id}/dataaddress")
    jsonpath_expr = parse('$.endpoint')
    data_endpoint=try_find_value_in_json(jsonpath_expr, response_json)
    jsonpath_expr = parse('$.authorization')
    token=try_find_value_in_json(jsonpath_expr, response_json)
    print(f"  Endpoint: {data_endpoint} Token: {token[:8] if token else None}...")
    if (not data_endpoint or not token):
        if (time.time()+1 < endtime):
            print(f"    no data endpoint found, poll again in a second ({count})")
            time.sleep(1)
        else:
            print(f"  timeout reached")
    else:
        break
if (not data_endpoint or not token):
    logging.error(f"Found an unexpected data endpoint: {data_endpoint} with token: {token}")
    exit(1)

print(f"8.2. GET THE DATA CONTENT FROM THE REST API")
response = requests.get(url=data_endpoint, headers={"Authorization": token})
print(f"Headers:\n{pretty_repr(dict(response.headers))}")
print(f"Content:\n{pretty_repr(response.content)}")

print("done.")



