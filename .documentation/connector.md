# Example messages for the EDC connector

Data exchange with the EDC connector follows the specification of the International Dataspace Protocol.

Reading data from a data source is a stepwise process that initially requires the provider of the data to make it discoverable to consumers of the data space.

Once consumers have discovered the published asset, they can negotiate to fetch the data.

1. Create asset [PROVIDER]
2. Create policy [PROVIDER]
3. Create contract [PROVIDER]
4. Fetch catalog [CONSUMER]
5. Negotiate contract [CONSUMER]
6. Get contract agreement ID [CONSUMER]
7. Start the data transfer [CONSUMER]
8. Get the data [CONSUMER]
   1. Get the data address [CONSUMER]
   2. Get the actual data content [CONSUMER]

The workflow above is called `Consumer-PULL` in the language of the Eclipse Data Components. It is the consumer who initializes the data transfer, and finally fetches (_pulls_) the data content as the last step.

The examples below were extracted from the [documentation about the EDC samples applications](https://github.com/eclipse-edc/Samples).

The [prerequisites documentation](https://github.com/eclipse-edc/Samples/blob/main/transfer/transfer-00-prerequisites/README.md) describes how to build and run the EDC connector as consumer or provider. Our startup configuration determines if it is consumer or provider. Both share the same source code.

Once a consumer and a provider are running, you can establish a data exchange via HTTP with a REST endpoint as data source using the following steps.

In the examples below, consumer and provider are running on the same machine. The consumer listens to ports `2919x` and the provider listens to ports `1919x`.

## 1. Create asset [PROVIDER]

`curl -d @path/to/create-asset.json -H 'content-type: application/json ' http://localhost:19193/management/v3/assets -s | jq`

**create-asset.json**

```
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
  },
  "@id": "myAsset",
  "properties": {
    "name": "REST endpoint",
    "contenttype": "application/json"
  },
  "dataAddress": {
    "type": "HttpData",
    "name": "myAssetAddress",
    "baseUrl": "https://path.of.rest/endpoint",
    "proxyPath": "true",
    "proxyMethod": "true",
    "proxyBody": "true",
    "proxyQueryParams": "true",
    "authKey": "ApiKey",
    "authCode": "{{APIKEY}}",
    "method": "POST"
  }
}
```

`baseUrl` is the URL of the REST endpoint, It is the data source from where the data will be finally fetched.

`proxyPath`, `proxyMethod`, `proxyBody`, `proxyQueryParams` are flags that allow a provider to behave as a proxy that forwards requests to the actual data source.

In the example above, the provider will be a proxy for the consumer. The provider will fetch the data from the data source. The REST call from the provider to the data source will use the value of `authKey` as part of its header, toghether with the value of `authCode` as the token.

The secret token is known to the provider but not to the consumer.

**Example Response**

```
{
  "@type": "IdResponse",
  "@id": "myAsset",
  "createdAt": 1727429239982,
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  }
}
```

## 2. Create policy [PROVIDER]

`curl -d @path/to/create-policy.json -H 'content-type: application/json' http://localhost:19193/management/v3/policydefinitions -s | jq`

**create-policy.json**

```
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  },
  "@id": "myPolicy",
  "policy": {
    "@context": "http://www.w3.org/ns/odrl.jsonld",
    "@type": "Set",
    "permission": [],
    "prohibition": [],
    "obligation": []
  }
}
```

Thre policy above does not specify special permissions, prohibitions, or oblogations. By default, all access is allowed.

**Example response**

```
{
  "@type": "IdResponse",
  "@id": "myPolicy",
  "createdAt": 1727429399794,
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  }
}
```

## 3. Create contract [PROVIDER]

`curl -d @path/to/create-contract-definition.json -H 'content-type: application/json' http://localhost:19193/management/v3/contractdefinitions -s | jq`

**create-contract-definition.json**

```
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
  },
  "@id": "1",
  "accessPolicyId": "myPolicy",
  "contractPolicyId": "myPolicy",
  "assetsSelector": []
}
```

The contract above does not specify any assets to which the policy is connected. By default, it is all assets.

**Example response**

```
{
  "@type": "IdResponse",
  "@id": "myPolicy",
  "createdAt": 1727429399794,
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  }
}
```

## 4. Fetch catalog [CONSUMER]

```
curl -X POST "http://localhost:29193/management/v3/catalog/request" -H 'Content-Type: application/json' -d @path/to/fetch-catalog.json -s | jq
```

**Example response**

```
{
  "@id": "773be931-40ff-410c-b6cb-346e3f6eaa6f",
  "@type": "dcat:Catalog",
  "dcat:dataset": {
    "@id": "myAsset",
    "@type": "dcat:Dataset",
    "odrl:hasPolicy": {
      "@id": "MQ==:YXNzZXRJZA==:ODJjMTc3NWItYmQ4YS00M2UxLThkNzItZmFmNGUyMTA0NGUw",
      "@type": "odrl:Offer",
      "odrl:permission": [],
      "odrl:prohibition": [],
      "odrl:obligation": []
    },
    "dcat:distribution": [
      {
        "@type": "dcat:Distribution",
        "dct:format": {
          "@id": "HttpData-PULL"
        },
        "dcat:accessService": {
          "@id": "17b36140-2446-4c23-a1a6-0d22f49f3042",
          "@type": "dcat:DataService",
          "dcat:endpointDescription": "dspace:connector",
          "dcat:endpointUrl": "http://localhost:19194/protocol"
        }
      },
      {
        "@type": "dcat:Distribution",
        "dct:format": {
          "@id": "HttpData-PUSH"
        },
        "dcat:accessService": {
          "@id": "17b36140-2446-4c23-a1a6-0d22f49f3042",
          "@type": "dcat:DataService",
          "dcat:endpointDescription": "dspace:connector",
          "dcat:endpointUrl": "http://localhost:19194/protocol"
        }
      }
    ],
    "name": "product description",
    "id": "myAsset",
    "contenttype": "application/json"
  },
  "dcat:distribution": [],
  "dcat:service": {
    "@id": "17b36140-2446-4c23-a1a6-0d22f49f3042",
    "@type": "dcat:DataService",
    "dcat:endpointDescription": "dspace:connector",
    "dcat:endpointUrl": "http://localhost:19194/protocol"
  },
  "dspace:participantId": "provider",
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "dcat": "http://www.w3.org/ns/dcat#",
    "dct": "http://purl.org/dc/terms/",
    "odrl": "http://www.w3.org/ns/odrl/2/",
    "dspace": "https://w3id.org/dspace/v0.8/"
  }
}
```

The consumer finds available assets in the catalog response. In the example above, the relevant identifier for the next steps of the data exchange is the offer id at the JSON path: `./"dcat:dataset"."odrl:hasPolicy"."@id"` (here: `MQ==:YXNz...NGUw`).

## 5. Negotiate contract [CONSUMER]

`curl -d @path/to/negotiate-contract.json -X POST -H 'content-type: application/json' http://localhost:29193/management/v3/contractnegotiations -s | jq`

**negotiate-contract.json**

```
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
  },
  "@type": "ContractRequest",
  "counterPartyAddress": "http://localhost:19194/protocol",
  "protocol": "dataspace-protocol-http",
  "policy": {
    "@context": "http://www.w3.org/ns/odrl.jsonld",
    "@id": "MQ==:YXNzZXRJZA==:ODJjMTc3NWItYmQ4YS00M2UxLThkNzItZmFmNGUyMTA0NGUw",
    "@type": "Offer",
    "assigner": "provider",
    "target": "myAsset"
  }
}
```

The request for negotiating a contract about the upcoming data exchange contains the offer id from the previous catalog response.

**Example response**

```
{
  "@type": "IdResponse",
  "@id": "96e58145-b39f-422d-b3f4-e9581c841678",
  "createdAt": 1727429786939,
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  }
}
```

## 6. Get contract agreement ID [CONSUMER]

`curl -X GET "http://localhost:29193/management/v3/contractnegotiations/96e58145-b39f-422d-b3f4-e9581c841678" --header 'Content-Type: application/json' -s | jq`

The negotiation of a contract happens asynchronously. At the end of the negotiation process, the consumer can get the agreement ID by calling the `contractnegotiations` API with the negotiation ID from the previous response (here: `96e58145-b39f-422d-b3f4-e9581c841678`).

**Example response**

```
{
  "@type": "ContractNegotiation",
  "@id": "96e58145-b39f-422d-b3f4-e9581c841678",
  "type": "CONSUMER",
  "protocol": "dataspace-protocol-http",
  "state": "FINALIZED",
  "counterPartyId": "provider",
  "counterPartyAddress": "http://localhost:19194/protocol",
  "callbackAddresses": [],
  "createdAt": 1727429786939,
  "contractAgreementId": "441353d9-9e86-4011-9828-63b2552d1bdd",
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  }
}
```

## 7. Start data transfer [CONSUMER]

`curl -X POST "http://localhost:29193/management/v3/transferprocesses" -H "Content-Type: application/json" -d @path/to/start-transfer.json -s | jq`

**start-transfer.json**

```
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
  },
  "@type": "TransferRequestDto",
  "connectorId": "provider",
  "counterPartyAddress": "http://localhost:19194/protocol",
  "contractId": "441353d9-9e86-4011-9828-63b2552d1bdd",
  "assetId": "myAsset",
  "protocol": "dataspace-protocol-http",
  "transferType": "HttpData-PULL"
}
```

The consumer initiates the data transfer process with a POST message containing the agreement ID from the previous response (here: `441353d9-9e86-4011-9828-63b2552d1bdd`), the asset identifier of the wanted data asset, and the transfer type.

**Example response**

```
{
  "@type": "IdResponse",
  "@id": "84eb10cc-1192-408c-a378-9ef407c156a0",
  "createdAt": 1727430075515,
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  }
}
```

## 8. Get the data [CONSUMER]

## 8.1. Get the data address [CONSUMER]

`curl http://localhost:29193/management/v3/edrs/84eb10cc-1192-408c-a378-9ef407c156a0/dataaddress | jq`

The result of starting a transfer process is a `DataAddress` that the consumer can get by calling the endpoint data reference API (`edrs`) with the identifier of the started transfer process (here: `84eb10cc-1192-408c-a378-9ef407c156a0`).

**Example response**

```
{
  "@type": "DataAddress",
  "type": "https://w3id.org/idsa/v4.1/HTTP",
  "endpoint": "http://localhost:19291/public",
  "authType": "bearer",
  "endpointType": "https://w3id.org/idsa/v4.1/HTTP",
  "authorization": "eyJraWQiOiJwdW...7Slukn1OMJw",
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  }
}
```

## 8.2. Get the data content [CONSUMER]

`curl --location --request GET 'http://localhost:19291/public/' --header 'Authorization: eyJraWQiOiJwdWJsaW...7Slukn1OMJw'`

The consumer can fetch the data about the wanted data asset by calling the endpoint that is included in the previous data address response (here: `http://localhost:19291/public`). The previous response also includes the required access token (here: `eyJraWQiOiJwdWJsaW...7Slukn1OMJw` shortened).

The response of this request is the actual data content from the data source.

The consumer calls the public API endpoint that was shared by the provider. The request is authorized with the token that was shared as well.
The provider now fetches the data from the data source based on the configuration of the data asset from the first step, and forwards the response to the consumer.

# Example script

The Python script [connector.py](./assets/connector/connector.py) executes the data exchange process as described above step by step.

It makes use of the JSON files in the folder [./assets/connector/](./assets/connector/).
