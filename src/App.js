import React from 'react';
import './App.css';
import Vote from './components/Vote';


function App() {
  return (
    <div className="App">
      <h1 class="heading">Hello</h1>
      <div class="container">
        <Vote candidate="Paul" address="Wien" />
        <Vote candidate="Heinz" address="Bregenz" />
        <Vote candidate="Wolfi" address="Lauterach" />
      </div>
    </div>
  );
}

export default App;
