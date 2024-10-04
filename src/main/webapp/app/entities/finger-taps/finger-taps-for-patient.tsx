import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getFingerTapsByPatient } from './finger-taps.reducer';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, ScatterChart } from 'recharts';

const generateRandomData = () => {
  const randomData = [];
  for (let i = 0; i < 10; i++) {
    randomData.push({
      date: `2024-09-1${i}`,
      thumbX: Math.random() * 100,
      thumbY: Math.random() * 100,
      digitX: Math.random() * 100,
      digitY: Math.random() * 100,
    });
  }
  return randomData;
};

const FingerTapsForPatient = () => {
  const { patientId } = useParams<{ patientId: string }>();
  const dispatch = useAppDispatch();

  // Fetch the FingerTaps data for the patient
  useEffect(() => {
    dispatch(getFingerTapsByPatient(patientId));
  }, [patientId]);

  // Retrieve the finger taps data from the state
  const fingerTaps = useAppSelector(state => state.fingerTaps.entities);

  // Check if there is any valid data, if not, use random data
  const chartData =
    fingerTaps.length > 0
      ? fingerTaps.flatMap(tap => {
          const thumbX = JSON.parse(tap.thumbX);
          const thumbY = JSON.parse(tap.thumbY);
          const digitX = JSON.parse(tap.digitX);
          const digitY = JSON.parse(tap.digitY);

          return thumbX.map((_, index) => ({
            date: `${tap.date}-${index}`, // Add index to differentiate points
            thumbX: thumbX[index],
            thumbY: thumbY[index],
            digitX: digitX[index],
            digitY: digitY[index],
          }));
        })
      : generateRandomData(); // Fallback to random data if the API data is missing or empty

  // return (
  //   <div>
  //     <h2>FingerTaps Data for Patient {patientId}</h2>
  //
  //     {/* Display the chart */}
  //     <ResponsiveContainer width="100%" height={400}>
  //       <ScatterChart>
  //         <CartesianGrid strokeDasharray="3 3" />
  //         <XAxis dataKey="date" />
  //         <YAxis />
  //         <Tooltip />
  //         <Legend />
  //         <Scatter name="Thumb X" data={chartData} dataKey="thumbX" fill="#8884d8" />
  //         <Scatter name="Thumb Y" data={chartData} dataKey="thumbY" fill="#82ca9d" />
  //         <Scatter name="Digit X" data={chartData} dataKey="digitX" fill="#ffc658" />
  //         <Scatter name="Digit Y" data={chartData} dataKey="digitY" fill="#ff7300" />
  //       </ScatterChart>
  //     </ResponsiveContainer>
  //   </div>
  // );
  return (
    <div>
      <h2>FingerTaps Data for Patient {patientId}</h2>

      {/* Display the chart */}
      <ResponsiveContainer width="100%" height={400}>
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="thumbX" stroke="#8884d8" />
          <Line type="monotone" dataKey="thumbY" stroke="#82ca9d" />
          <Line type="monotone" dataKey="digitX" stroke="#ffc658" />
          <Line type="monotone" dataKey="digitY" stroke="#ff7300" />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default FingerTapsForPatient;
