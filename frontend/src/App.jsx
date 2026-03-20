import React, { useState, useEffect } from 'react';
import BookingModal from "./BookingModal.jsx";

function App() {
    const [guests, setGuests] = useState(2);
    const [preferences, setPreferences] = useState([]);
    const [tables, setTables] = useState([]);
    const [occupiedIds, setOccupiedIds] = useState([]); // Store occupied IDs separately
    const [recommendation, setRecommendation] = useState(null);
    const [loading, setLoading] = useState(false);
    const [selectedTable, setSelectedTable] = useState(null);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [features, setFeatures] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/api/tables/features')
            .then(res => res.json())
            .then(data => setFeatures(data))
            .catch(err => console.error("Failed to fetch features", err));
    }, []);

    const searchTables = async () => {
        setIsDropdownOpen(false);
        setLoading(true);
        try {
            const prefsQuery = preferences.length > 0 ? `&prefs=${preferences.join(',')}` : '';
            const response = await fetch(`http://localhost:8080/api/tables/availableFor?guests=${guests}${prefsQuery}`);
            const data = await response.json();

            // Update states based on the new JSON structure
            setTables(data.allTables || []);
            setOccupiedIds(data.occupiedTableIds || []);
            setRecommendation(data.suggestedTableId);
        } catch (error) {
            console.error("Error fetching tables:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 p-6 font-sans text-gray-900">
            <div className="max-w-6xl mx-auto">
                <header className="mb-8 border-b pb-4">
                    <h1 className="text-3xl font-extrabold text-gray-800">Restaurant Table Booking</h1>
                    <p className="text-gray-500 text-sm mt-1">Select your preferences to find the perfect spot.</p>
                </header>

                {/* Filter Section */}
                <div className="flex flex-wrap items-end gap-6 mb-10 bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <div className="flex flex-col gap-2">
                        <label className="text-xs font-bold text-gray-500 uppercase tracking-widest">Guests</label>
                        <input
                            type="number"
                            value={guests}
                            onChange={(e) => setGuests(e.target.value)}
                            min="1"
                            className="w-24 p-2.5 bg-gray-50 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all"
                        />
                    </div>

                    <div className="relative">
                        <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2">Preferences</label>
                        <button
                            onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                            className="flex items-center justify-between min-w-50 p-2.5 bg-white border border-gray-300 rounded-lg hover:border-blue-400 transition-colors"
                        >
              <span className="text-gray-700">
                {preferences.length > 0 ? `Filters (${preferences.length})` : 'Select Filters'}
              </span>
                            <span className={`transition-transform ${isDropdownOpen ? 'rotate-180' : ''}`}>▼</span>
                        </button>

                        {isDropdownOpen && (
                            <div className="absolute top-full left-0 mt-2 w-64 bg-white border border-gray-200 rounded-xl shadow-xl z-20 p-4">
                                <div className="flex flex-col gap-3">
                                    {features.map(f => (
                                        <label key={f.id} className="flex items-center gap-3 cursor-pointer group">
                                            <input
                                                type="checkbox"
                                                className="w-5 h-5 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                                                checked={preferences.includes(f.id)}
                                                onChange={() => {
                                                    const nextPreferences = preferences.includes(f.id)
                                                        ? preferences.filter(p => p !== f.id)
                                                        : [...preferences, f.id];
                                                    setPreferences(nextPreferences);
                                                }}
                                            />
                                            <span className="text-gray-700 group-hover:text-blue-600 transition-colors">{f.label}</span>
                                        </label>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>

                    <button
                        onClick={searchTables}
                        className="px-8 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-lg shadow-md transition-all active:scale-95"
                    >
                        Find Tables
                    </button>
                </div>

                {/* Visual Floor Plan Section */}
                <section>
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-2xl font-bold text-gray-700 font-serif">Floor Plan</h2>
                        <div className="flex gap-4 text-[10px] font-bold uppercase text-gray-400">
                            <span className="flex items-center gap-1"><span className="w-3 h-3 bg-green-100 border border-green-300 rounded-sm"></span> Free</span>
                            <span className="flex items-center gap-1"><span className="w-3 h-3 bg-red-100 border border-red-300 rounded-sm"></span> Booked</span>
                        </div>
                    </div>

                    {loading ? (
                        <div className="flex justify-center py-20">
                            <div className="animate-spin rounded-full h-10 w-10 border-t-2 border-b-2 border-blue-600"></div>
                        </div>
                    ) : (
                        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
                            {tables.map(table => {
                                const isRecommended = table.id === recommendation;
                                const isOccupied = occupiedIds.includes(table.id); // Updated logic here

                                return (
                                    <div
                                        key={table.id}
                                        onClick={() => !isOccupied && setSelectedTable(table.id)}
                                        className={`
                      relative flex flex-col items-center justify-center p-6 h-40 rounded-3xl border-2 transition-all duration-300
                      ${isOccupied
                                            ? 'bg-gray-100 border-gray-200 cursor-not-allowed grayscale'
                                            : 'bg-white border-white shadow-sm cursor-pointer hover:shadow-xl hover:-translate-y-1 hover:border-blue-200'}
                      ${isRecommended ? 'ring-4 ring-yellow-400 ring-offset-2 border-yellow-400' : ''}
                    `}
                                    >
                                        {isRecommended && (
                                            <div className="absolute -top-3 left-1/2 -translate-x-1/2 bg-yellow-400 text-yellow-900 text-[10px] font-black px-4 py-1 rounded-full shadow-md z-10 whitespace-nowrap">
                                                ⭐ BEST CHOICE
                                            </div>
                                        )}

                                        <span className="text-4xl font-black text-gray-800">{table.id}</span>
                                        <div className="mt-2 text-center">
                                            <p className="text-sm font-semibold text-gray-500">{table.seats} Seats</p>
                                            <p className="text-[10px] font-medium text-gray-400 italic">{table.zone}</p>
                                        </div>

                                        <div className={`mt-3 px-3 py-0.5 rounded-full text-[9px] font-bold uppercase tracking-wider ${isOccupied ? 'bg-red-100 text-red-600' : 'bg-green-100 text-green-700'}`}>
                                            {isOccupied ? 'Occupied' : 'Available'}
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    )}
                </section>

                {selectedTable && (
                    <BookingModal
                        tableId={selectedTable}
                        onClose={() => setSelectedTable(null)}
                    />
                )}
            </div>
        </div>
    );
}

export default App;