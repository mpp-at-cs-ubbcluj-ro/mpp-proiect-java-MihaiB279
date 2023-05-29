import React, {useEffect} from 'react';
import {useState} from 'react';
import FlightsTable from './FlightsTable.jsx';
import './FlightsApp.css'
import FlightsForm from "./FlightsForm.jsx";
import {GetFlights, DeleteFlight, AddFlight, UpdateFlight} from './utils/rest-calls'


export default function FlightsApp() {
    const [flights, setFlights] = useState([{"id":"2", "destination":"aaa", "departureDateTime":"2023-03-20 00:16:37.148578", "airport":"asdasd", "availanbleSeats":"12"}]);

    useEffect(() => {
        console.log('inside useEffect')
        GetFlights().then(flights => setFlights(flights));
    }, []);

    function addFunc(flight) {
        console.log('inside addFunc ' + flight);
        AddFlight(flight)
            .then(res => GetFlights())
            .then(flights => setFlights(flights))
            .catch(erorr => console.log('eroare add ', erorr));
    }

    function deleteFunc(flight) {
        console.log('inside deleteFunc ' + flight);
        DeleteFlight(flight)
            .then(res => GetFlights())
            .then(flights => setFlights(flights))
            .catch(error => console.log('eroare delete', error));
    }

    function updateFunc(flight) {
        console.log('inside updateFunc ' + flight);
        UpdateFlight(flight)
            .then(res => GetFlights())
            .then(flights => setFlights(flights))
            .catch(error => console.log('eroare update', error));
    }

    return (<div className="FlightsApp">
        <h1> New Flights Management App </h1>
        <FlightsForm addFunc={addFunc}/>
        <br/>
        <br/>
        <FlightsTable flights={flights} updateFunc={updateFunc} deleteFunc={deleteFunc}/>
    </div>);
}
