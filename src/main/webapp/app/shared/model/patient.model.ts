import dayjs from 'dayjs';

export interface IPatient {
  id?: string;
  gender?: string | null;
  birthDate?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  yearOfDiagnosis?: number | null;
}

export const defaultValue: Readonly<IPatient> = {};
