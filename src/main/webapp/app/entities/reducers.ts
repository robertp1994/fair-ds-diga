import patient from 'app/entities/patient/patient.reducer';
import fingerTaps from 'app/entities/finger-taps/finger-taps.reducer';
import scores from 'app/entities/scores/scores.reducer';
import symptoms from 'app/entities/symptoms/symptoms.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  patient,
  fingerTaps,
  scores,
  symptoms,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
