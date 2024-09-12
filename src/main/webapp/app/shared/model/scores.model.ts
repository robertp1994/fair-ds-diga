import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';

export interface IScores {
  id?: number;
  time?: dayjs.Dayjs | null;
  questionnaire?: string | null;
  score?: number | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IScores> = {};
