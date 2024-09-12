import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';
import { SIDE } from 'app/shared/model/enumerations/side.model';

export interface IFingerTaps {
  id?: number;
  patientId?: string | null;
  date?: dayjs.Dayjs | null;
  side?: keyof typeof SIDE | null;
  thumbX?: string | null;
  thumbY?: string | null;
  digitX?: string | null;
  digitY?: string | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IFingerTaps> = {};
