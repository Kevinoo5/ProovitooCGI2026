import React, { useState, useEffect } from 'react';

function App() {
  // State for filters
  const [guests, setGuests] = useState(2);
  const [preference, setPreference] = useState('');

  // State for data from Spring Boot
  const [tables, setTables] = useState([]);
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(false);

  // Function to fetch tables based on filters
  const searchTables = async () => {
    setLoading(true);
    try {
      // Replace with your actual endpoint
      const response = await fetch(`http://localhost:8080/api/availableFor?guests=${guests}`);
      const data = await response.json();
      setTables(data.allTables); // Assuming backend sends all tables
      setRecommendation(data.suggestedTableId); // Highlighting the 'best' one
    } catch (error) {
      console.error("Error fetching tables:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
      <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
        <h1>Restaurant Table Booking</h1>

        {/* 1. Filter Section */}
        <div style={{ marginBottom: '30px', display: 'flex', gap: '15px', alignItems: 'center' }}>
          <label>
            Guests:
            <input type="number" value={guests} onChange={(e) => setGuests(e.target.value)} min="1" />
          </label>

          <select value={preference} onChange={(e) => setPreference(e.target.value)}>
            <option value="">No Preference</option>
            <option value="WINDOW">Window Seat</option>
            <option value="PRIVACY">Quiet Corner</option>
            <option value="ACCESSIBLE">Accessible</option>
          </select>

          <button onClick={searchTables} style={{ padding: '5px 15px', cursor: 'pointer' }}>
            Find Tables
          </button>
        </div>

        <hr />

        {/* 2. Visual Floor Plan Section */}
        <h2>Restaurant Floor Plan</h2>
        {loading ? <p>Loading tables...</p> : (
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(4, 150px)',
              gap: '20px',
              marginTop: '20px'
            }}>
              {tables.map(table => {
                const isRecommended = table.id === recommendation;
                const isOccupied = table.occupied;

                return (
                    <div
                        key={table.id}
                        style={{
                          height: '100px',
                          border: isRecommended ? '4px solid #FFD700' : '1px solid #ccc',
                          borderRadius: '8px',
                          display: 'flex',
                          flexDirection: 'column',
                          alignItems: 'center',
                          justifyContent: 'center',
                          backgroundColor: isOccupied ? '#ffcccc' : '#ccffcc',
                          boxShadow: isRecommended ? '0 0 15px rgba(255, 215, 0, 0.6)' : 'none',
                          cursor: isOccupied ? 'not-allowed' : 'pointer',
                          position: 'relative'
                        }}
                    >
                      {isRecommended && <span style={{ fontSize: '10px', fontWeight: 'bold', position: 'absolute', top: '5px' }}>⭐ BEST FIT</span>}
                      <strong>Table {table.tableNumber}</strong>
                      <span>Seats: {table.capacity}</span>
                      <small>{isOccupied ? 'Occupied' : 'Available'}</small>
                    </div>
                );
              })}
            </div>
        )}
      </div>
  );
}

export default App;