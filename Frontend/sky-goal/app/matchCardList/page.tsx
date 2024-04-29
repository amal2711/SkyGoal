async function getData() {
  const res = await fetch('https://api.sampleapis.com/coffee/hot')
  if (!res.ok) {
    throw new Error('Failed to fetch data')
  }
  return res.json()
}
export default async function matchCardList() {
  const weatherData = [
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' }, { date: '2024-04-28', competition: 'Soccer League', teamOne: 'NYFC', scoreOne: 3, teamTwo: 'CHI', scoreTwo: 1, time: '14:00', matchMinute: 'Full Time', location: 'New York', temperature: 72, weatherCondition: 'Sunny' },
    { date: '2024-04-29', competition: 'Baseball Series', teamOne: 'Cubs', scoreOne: 5, teamTwo: 'Giants', scoreTwo: 2, time: '17:00', matchMinute: '7th Inning', location: 'Chicago', temperature: 65, weatherCondition: 'Cloudy' },
    { date: '2024-04-30', competition: 'Marathon', teamOne: 'Runner Group A', scoreOne: '-', teamTwo: 'Runner Group B', scoreTwo: '-', time: '09:00', matchMinute: 'Completed', location: 'San Francisco', temperature: 55, weatherCondition: 'Rainy' },
    { date: '2024-05-01', competition: 'Basketball Playoffs', teamOne: 'Celtics', scoreOne: 112, teamTwo: 'Nets', scoreTwo: 108, time: '20:00', matchMinute: '4th Quarter', location: 'Boston', temperature: 60, weatherCondition: 'Windy' },
    
  ];

    const data = await getData()
  

  return (
    <div className="  bg-dark-blue text-white ">
        <h1 className="text-5xl font-bold pl-6 pb-3 ml-5 pt-5 w-[95%] border-b ">SkyGoal</h1>
        <div className=" ml-[79%] ">
        <input type="text" className="mb-7 translate-y-[-45px] rounded-xl text-black pl-2" placeholder="Search"/>
      </div>
       <div className="px-10 ">
      <table className="w-full text-left">
        <thead>
          <tr>
            <th>Date</th>
            <th>Competition</th>
            <th>Teams and Scores</th>
            <th>Time</th>
            <th>Match Minute</th>
            <th>Location</th>
            <th>Temp(°F)</th>
            <th>Condition</th>
          </tr>
        </thead>
        <tbody>
          {weatherData.map((item, index) => (
            <tr key={index} className="w-1/9">
              <td>{item.date}</td>
              <td>{item.competition}</td>
              <td>{`${item.teamOne} ${item.scoreOne} - ${item.scoreTwo} ${item.teamTwo}`}</td>
              <td>{item.time}</td>
              <td>{item.matchMinute}</td>
              <td>{item.location}</td>
              <td>{item.temperature}°F</td>
              <td>{item.weatherCondition}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </div>
  );
}
