import React, { useState, useEffect } from 'react';

function App() {
  // State for filters
  const [guests, setGuests] = useState(2);
  const [preferences, setPreferences] = useState([]);
  const [tables, setTables] = useState([]);
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(false);

  const [features, setFeatures] = useState([])

  useEffect(() => {
    fetch('http://localhost:8080/api/features')
        .then(res => res.json())
        .then(data => setFeatures(data));
  }, []);

  const searchTables = async () => {
    setLoading(true);
    try {
      const prefsQuery = preferences.length > 0 ? `&prefs=${preferences.join(',')}` : '';
      const response = await fetch(`http://localhost:8080/api/availableFor?guests=${guests}${prefsQuery}`);

      const data = await response.json();
      setTables(data.allTables);
      setRecommendation(data.suggestedTableId);
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

          <div>
            {features.map(f => (
                <label key={f.id} style={{ display: 'block', marginBottom: '5px' }}>
                  <input
                      type="checkbox"
                      checked={preferences.includes(f.id)}
                      onChange={() => {
                        const nextPreferences = preferences.includes(f.id)
                            ? preferences.filter(p => p !== f.id)
                            : [...preferences, f.id];

                        setPreferences(nextPreferences);
                      }}
                  />
                  {f.label}
                </label>
            ))}
          </div>
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
                      <strong>Table {table.id}</strong>
                      <span>Seats: {table.seats}</span>
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