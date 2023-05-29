import React, {useState} from "react";
import './FlightsApp.css';

function FlightRow({flight, updateFunc, deleteFunc}) {
    const [updateDestination, setUpdateDestination] = useState(flight.destination);
    const [updateDepartureDateTime, setUpdateDepartureDateTime] = useState(flight.departureDateTime);
    const [updateAirport, setUpdateAirport] = useState(flight.airport);
    const [updateAvailableSeats, setUpdateAvailableSeats] = useState(flight.availableSeats);
    const [isEditing,setIsEditing]=useState(false);
    function handleUpdate(event) {
        flight.destination - updateDestination;
        flight.departureDateTime = updateDepartureDateTime;
        flight.airport = updateAirport;
        flight.availableSeats = updateAvailableSeats;
        console.log('upate button pentru ' + flight.id);
        updateFunc(flight);
    }

    function handleDelete(event) {
        console.log('delete button pentru ' + flight.id);
        deleteFunc(flight.id);
    }

    const handleEdit=()=>{
        setIsEditing(true);
    };
    const handleCancel=()=>{
        if (isEditing){
            setIsEditing(false);
        }
        setUpdateDestination(flight.destination);
        setUpdateDepartureDateTime(flight.departureDateTime);
        setUpdateAirport(flight.airport);
        setUpdateAvailableSeats(flight.availableSeats);
        setUpdateAvailableSeats(flight.availableSeats);
    };
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsEditing(false);
        handleUpdate();
    };

    return (
        <tr>
            <td>{flight.id}</td>
            <td>
                {isEditing ? (
                    <input
                        type="text"
                        value={updateDestination}
                        onChange={(event)=>setUpdateDestination(event.target.value)}
                    />
                ) : (
                    flight.destination
                )}
            </td>
            <td>
                {isEditing ? (
                    <input
                        type="text"
                        value={updateDepartureDateTime}
                        onChange={(event)=>setUpdateDepartureDateTime(event.target.value)}
                    />
                ) : (
                    flight.departureDateTime
                )}
            </td>
            <td>
                {isEditing ? (
                    <input
                        type="text"
                        value={updateAirport}
                        onChange={(event)=>setUpdateAirport(event.target.value)}
                    />
                ) : (
                    flight.airport
                )}
            </td>
            <td>
                {isEditing ? (
                    <input
                        type="text"
                        value={updateAvailableSeats}
                        onChange={(event)=>setUpdateAvailableSeats(event.target.value)}
                    />
                ) : (
                    flight.availableSeats
                )}
            </td>
            <td>
                {isEditing ? (
                    <>
                        <button onClick={handleSubmit}>Submit</button>
                        <button onClick={handleCancel}>Cancel</button>
                    </>
                ) : (
                    <>
                        <button onClick={handleEdit}>Edit</button>
                        <button onClick={handleDelete}>Delete</button>
                    </>
                )}
            </td>


        </tr>

    );
}

export default function FlightsTable({flights, updateFunc, deleteFunc}) {
    console.log("In FlightsTable");
    console.log(flights);
    let rows = [];

    flights.forEach(function (flight) {
        rows.push(<FlightRow flight={flight} key={flight.id} updateFunc={updateFunc} deleteFunc={deleteFunc}/>);
    });
    return (
        <div className="FlightsTable">

            <table className="center">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Destination</th>
                    <th>Departure</th>
                    <th>Airport</th>
                    <th>availableSeats</th>

                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>

        </div>
    );
}