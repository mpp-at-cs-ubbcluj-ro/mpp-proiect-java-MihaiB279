// StateContext.js
import React, { createContext, useState } from "react";

export const StateContext = createContext();

export const StateProvider = ({ children }) => {
    const [destination, setDestination] = useState('');
    const [departureDateTime, setDepartureDateTime] = useState('');
    const [airport, setAirport] = useState('');
    const [availableSeats, setAvailableSeats] = useState('');

    return (
        <StateContext.Provider
            value={{
                destination,
                setDestination,
                departureDateTime,
                setDepartureDateTime,
                airport,
                setAirport,
                availableSeats,
                setAvailableSeats
            }}
        >
            {children}
        </StateContext.Provider>
    );
};
