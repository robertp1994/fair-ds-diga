import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';
import { STATUS } from 'app/shared/model/enumerations/status.model';

export interface ISymptoms {
  id?: number;
  time?: dayjs.Dayjs | null;
  status?: keyof typeof STATUS | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<ISymptoms> = {};
