import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const FingertapsChart = ({ data }) => {
  // Combine the thumbX and thumbY coordinates into a format suitable for charting
  const chartData = data.map((tap, index) => ({
    date: tap.date,
    thumbX: tap.thumbX[0], // Taking the first element as an example
    thumbY: tap.thumbY[0],
    digitX: tap.digitX[0],
    digitY: tap.digitY[0],
  }));

  return (
    <ResponsiveContainer width="100%" height={400}>
      <LineChart data={chartData} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="date" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Line type="monotone" dataKey="thumbX" stroke="#8884d8" activeDot={{ r: 8 }} />
        <Line type="monotone" dataKey="thumbY" stroke="#82ca9d" />
        <Line type="monotone" dataKey="digitX" stroke="#ffc658" />
        <Line type="monotone" dataKey="digitY" stroke="#ff7300" />
      </LineChart>
    </ResponsiveContainer>
  );
};

export default FingertapsChart;
