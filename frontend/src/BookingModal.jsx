import React, { useState, useEffect } from 'react';

const START_HOUR = 7;
const END_HOUR = 21;
const TOTAL_HOURS = END_HOUR - START_HOUR + 1;
const TOTAL_ROWS = TOTAL_HOURS * 2; // 30-min slots
const HOURS = Array.from({ length: TOTAL_HOURS }, (_, i) => START_HOUR + i);

const BookingModal = ({ tableId, onClose }) => {
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isSelecting, setIsSelecting] = useState(false);
    const [draftSelection, setDraftSelection] = useState(null);
    const [pendingBooking, setPendingBooking] = useState(null);

    const [currentWeekStart, setCurrentWeekStart] = useState(() => {
        const today = new Date();
        const day = today.getDay() || 7;
        today.setDate(today.getDate() - day + 1);
        today.setHours(0, 0, 0, 0);
        return today;
    });

    const weekDays = Array.from({ length: 7 }).map((_, i) => {
        const date = new Date(currentWeekStart);
        date.setDate(date.getDate() + i);
        return date;
    });

    useEffect(() => {
        if (!tableId) return;
        setLoading(true);
        fetch(`http://localhost:8080/api/bookings/${tableId}`)
            .then(res => res.json())
            .then(data => {
                const rawData = Array.isArray(data) ? data : (data ? [data] : []);

                // TRANSFORM THE DATA HERE
                const formattedData = rawData.map(booking => {
                    const start = new Date(booking.startTime);
                    const end = new Date(booking.endTime);

                    return {
                        ...booking,
                        // Convert "2026-03-20T18:00:59" -> "18:00"
                        startTime: `${start.getHours().toString().padStart(2, '0')}:${start.getMinutes().toString().padStart(2, '0')}`,
                        endTime: `${end.getHours().toString().padStart(2, '0')}:${end.getMinutes().toString().padStart(2, '0')}`,
                        // Get day index (0 = Monday, 6 = Sunday)
                        // getDay() returns 0 for Sunday, so we adjust it:
                        dayIndex: start.getDay() === 0 ? 6 : start.getDay() - 1
                    };
                });

                setBookings(formattedData);
                setLoading(false);
            })
            .catch(err => {
                console.error("Viga:", err);
                setLoading(false);
            });
    }, [tableId, currentWeekStart]);

    const rowIndexToTime = (index) => {
        const totalMinutes = index * 30;
        const h = START_HOUR + Math.floor(totalMinutes / 60);
        const m = totalMinutes % 60;
        return `${h.toString().padStart(2, '0')}:${m === 0 ? '00' : '30'}`;
    };

    const handleMouseDown = (dayIndex, rowIndex) => {
        if (pendingBooking) return;
        setIsSelecting(true);
        setDraftSelection({ dayIndex, startRow: rowIndex, endRow: rowIndex + 1 });
    };

    const handleMouseEnter = (dayIndex, rowIndex) => {
        if (isSelecting && draftSelection && draftSelection.dayIndex === dayIndex) {
            setDraftSelection(prev => ({
                ...prev,
                endRow: rowIndex + 1
            }));
        }
    };

    const handleMouseUp = () => {
        if (isSelecting && draftSelection) {
            setPendingBooking({
                ...draftSelection,
                startTime: rowIndexToTime(draftSelection.startRow),
                endTime: rowIndexToTime(draftSelection.endRow)
            });
            setDraftSelection(null);
            setIsSelecting(false);
        }
    };

    const confirmBooking = () => {
        const newBooking = {
            id: Date.now(),
            dayIndex: pendingBooking.dayIndex,
            startTime: pendingBooking.startTime,
            endTime: pendingBooking.endTime,
            title: "Uus broneering"
        };
        setBookings(prev => [...prev, newBooking]);
        setPendingBooking(null);
    };

    const getGridPlacement = (startTime, endTime, dayIndex) => {
        // 1. Convert "10:00" to [10, 0]
        const [startH, startM] = startTime.split(':').map(Number);
        const [endH, endM] = endTime.split(':').map(Number);

        // 2. Calculate the Start Line
        // (Current Hour - Start Hour) * 2 (for 30 min slots) + 1 (for CSS 1-based index)
        // If start is 10:30, we add an extra 1.
        const startRow = (startH - START_HOUR) * 2 + (startM >= 30 ? 1 : 0) + 1;

        // 3. Calculate the End Line
        // If end is 18:00, (18-10)*2 = 16. + 1 = 17.
        const endRow = (endH - START_HOUR) * 2 + (endM >= 30 ? 1 : 0) + 1;

        return {
            gridRow: `${startRow} / ${endRow}`,
            gridColumn: `${(dayIndex ?? 0) + 1} / span 1`,
            position: 'relative',
            zIndex: 10,
            height: '100%'
        };
    };

    if (!tableId) return null;

    return (
        <div className="fixed inset-0 bg-slate-900/80 flex justify-center items-center z-100 p-4 backdrop-blur-sm">
            <div className="bg-white rounded-xl w-full max-w-5xl max-h-[90vh] overflow-hidden flex flex-col shadow-2xl border border-white/20">

                {/* Header */}
                <div className="p-6 border-b flex justify-between items-center bg-white">
                    <h2 className="text-2xl font-black text-slate-800">Laud {tableId}</h2>
                    <div className="flex items-center gap-4">
                        <span className="text-sm font-bold bg-slate-100 px-3 py-1 rounded-full text-slate-500 uppercase">
                            {weekDays[0].toLocaleDateString('et-EE')} — {weekDays[6].toLocaleDateString('et-EE')}
                        </span>
                        <button onClick={onClose} className="text-3xl text-slate-300 hover:text-rose-500 leading-none">&times;</button>
                    </div>
                </div>

                {loading ? (
                    <div className="p-20 text-center text-slate-400">Laadin...</div>
                ) : (
                    <div className="flex-1 overflow-hidden flex flex-col">
                        {/* Grid Header Labels */}
                        <div className="grid grid-cols-[60px_1fr]">
                            <div className="bg-slate-50 border-r border-slate-200"></div>
                            <div className="grid grid-cols-7 bg-slate-50 border-b border-slate-200">
                                {weekDays.map((date, i) => (
                                    <div key={i} className="py-3 text-center border-l border-slate-200 first:border-l-0">
                                        <div className="text-[10px] uppercase font-black text-slate-400">{date.toLocaleDateString('et-EE', { weekday: 'short' })}</div>
                                        <div className="text-lg font-bold text-slate-700">{date.getDate()}</div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        {/* Scrolling Body */}
                        <div className="grid grid-cols-[60px_1fr] overflow-y-auto flex-1 bg-white">
                            {/* Time Labels */}
                            <div className="bg-slate-50/50 border-r border-slate-200 select-none">
                                {HOURS.map(h => (
                                    <React.Fragment key={h}>
                                        <div className="h-7.5 text-[11px] font-bold text-slate-400 text-right pr-2 leading-7.5 border-b border-slate-50">{h}:00</div>
                                        <div className="h-7.5 text-[10px] text-slate-300 text-right pr-2 leading-7.5 border-b border-slate-100 italic">{h}:30</div>
                                    </React.Fragment>
                                ))}
                            </div>

                            {/* Main Interaction Grid */}
                            <div
                                className="grid grid-cols-7 relative"
                                style={{
                                    gridTemplateRows: `repeat(${TOTAL_ROWS}, 30px)`,
                                    userSelect: 'none' // Prevents text selection while dragging
                                }}
                                onMouseLeave={() => setIsSelecting(false)} // Safety: stop if mouse leaves grid
                                onMouseUp={handleMouseUp}
                            >
                                {/* 1. Column Backgrounds & Cells */}
                                {Array.from({ length: 7 }).map((_, colIndex) => (
                                    <React.Fragment key={`col-${colIndex}`}>
                                        {Array.from({ length: TOTAL_ROWS }).map((_, rowIndex) => (
                                            <div
                                                key={`cell-${colIndex}-${rowIndex}`}
                                                onMouseDown={() => handleMouseDown(colIndex, rowIndex)}
                                                onMouseEnter={() => handleMouseEnter(colIndex, rowIndex)}
                                                className={`h-7.5 cursor-cell hover:bg-blue-50/50 transition-colors border-r border-slate-100
                        ${rowIndex % 2 === 0 ? 'border-b border-dashed border-slate-100' : 'border-b border-solid border-slate-200'}`}
                                                style={{
                                                    gridColumn: colIndex + 1,
                                                    gridRow: rowIndex + 1
                                                }}
                                            />
                                        ))}
                                    </React.Fragment>
                                ))}

                                {/* 2. Active Dragging Overlay */}
                                {isSelecting && draftSelection && (
                                    <div
                                        className="absolute w-full bg-blue-500/30 border-2 border-blue-500 z-40 pointer-events-none rounded-md"
                                        style={{
                                            gridRowStart: Math.min(draftSelection.startRow, draftSelection.endRow - 1) + 1,
                                            gridRowEnd: Math.max(draftSelection.startRow, draftSelection.endRow - 1) + 2,

                                            gridColumn: `${draftSelection.dayIndex + 1} / span 1`,
                                            margin: '2px',
                                            height: 'calc(100% - 4px)'
                                        }}
                                    >
                                        {/* Visual Time Indicator */}
                                        <div className="absolute top-0 left-0 right-0 bg-blue-600 text-white text-[9px] font-bold px-1 py-0.5 rounded-t-sm truncate">
                                            {rowIndexToTime(draftSelection.startRow)} - {rowIndexToTime(draftSelection.endRow)}
                                        </div>
                                    </div>
                                )}

                                {/* 3. Pending Confirmation Overlay */}
                                {pendingBooking && (
                                    <div
                                        className="absolute inset-x-0 mx-1 z-30 bg-amber-50 border-2 border-amber-400 rounded-lg shadow-xl p-2 flex flex-col justify-between"
                                        style={{
                                            gridRow: `${pendingBooking.startRow + 1} / ${pendingBooking.endRow + 1}`,
                                            gridColumn: `${pendingBooking.dayIndex + 1} / span 1`,
                                        }}
                                    >
                                        <div className="flex flex-col leading-tight">
                                            <span className="text-[10px] font-black text-amber-600 uppercase">Confirm?</span>
                                            <span className="text-[11px] font-bold text-slate-700">{pendingBooking.startTime}-{pendingBooking.endTime}</span>
                                        </div>
                                        <div className="flex gap-1 mt-1">
                                            <button onClick={confirmBooking} className="flex-1 bg-emerald-500 hover:bg-emerald-600 text-white text-[10px] font-bold py-1 rounded">Broneeri</button>
                                            <button onClick={() => setPendingBooking(null)} className="px-2 bg-slate-200 text-slate-600 text-[10px] rounded">&times;</button>
                                        </div>
                                    </div>
                                )}

                                {/* 4. Confirmed Bookings */}
                                {bookings.map((b, index) => (
                                    <div
                                        key={b.id || index}
                                        className="absolute bg-indigo-500 border-l-4 border-indigo-700 mx-1 my-px rounded-md p-2 text-white z-10 shadow-md overflow-hidden"
                                        style={getGridPlacement(b.startTime, b.endTime, b.dayIndex)}
                                    >
                                        <div className="font-bold text-[11px] truncate">{b.title}</div>
                                        <div className="text-[10px] opacity-80">{b.startTime}-{b.endTime}</div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default BookingModal;