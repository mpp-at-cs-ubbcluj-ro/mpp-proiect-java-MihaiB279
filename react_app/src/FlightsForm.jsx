import React from  'react';
import { useState } from 'react';
export default function FlightsForm({addFunc}){

    const [destination, setDestination] = useState('');
    const [departureDateTime, setDepartureDateTime] = useState('');
    const [airport, setAirport] = useState('');
    const [availableSeats, setAvailableSeats] = useState('');

    function handleSubmit (event){

        let flight={
            destination:destination,
            departureDateTime:departureDateTime,
            airport:airport,
            availableSeats:availableSeats
        }
        console.log('A flight was submitted: ');
        console.log(flight);
        addFunc(flight);
        event.preventDefault();
    }
    return(
        <form onSubmit={handleSubmit}>
            <label>
                Destination:
                <input type="text" value={destination} onChange={e=>setDestination(e.target.value)} />
            </label><br/>
            <label>
                Departure:
                <input type="datetime-local" value={departureDateTime} onChange={e=>setDepartureDateTime(e.target.value)} />
            </label><br/>
            <label>
                Airport:
                <input type="text" value={airport} onChange={e=>setAirport(e.target.value)} />
            </label><br/>
            <label>
                Seats:
                <input type="text" value={availableSeats} onChange={e=>setAvailableSeats(e.target.value)} />
            </label><br/>

            <input type="submit" value="Add flight" />
        </form>);
}