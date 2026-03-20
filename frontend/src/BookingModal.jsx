import React from 'react';

const BookingModal = ({ tableId, bookingDate, bookingTime, guests, onClose }) => {

    const handleConfirm = async () => {
        const start = new Date(`${bookingDate}T${bookingTime}`);
        const end = new Date(start.getTime() + 60 * 60 * 1000);

        const bookingData = {
            tableId: tableId,
            startTime: start.toISOString(),
            endTime: end.toISOString(),
            guestCount: guests,
            title: `Reservation: Table ${tableId}`
        };

        try {
            const response = await fetch('http://localhost:8080/api/bookings', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(bookingData)
            });

            if (response.ok) {
                alert("Booking confirmed!");
                onClose();
            }
        } catch (err) {
            console.error("Booking failed", err);
        }
    };

    return (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50 p-4">
            <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full p-8">
                <h2 className="text-2xl font-bold text-gray-800 mb-4">Confirm Booking</h2>

                <div className="space-y-3 mb-8 bg-gray-50 p-4 rounded-lg">
                    <p className="text-gray-600"><strong>Table:</strong> {tableId}</p>
                    <p className="text-gray-600"><strong>Date:</strong> {bookingDate}</p>
                    <p className="text-gray-600"><strong>Time:</strong> {bookingTime}</p>
                    <p className="text-gray-600"><strong>Guests:</strong> {guests}</p>
                </div>

                <div className="flex gap-3">
                    <button
                        onClick={onClose}
                        className="flex-1 py-3 border border-gray-300 rounded-xl font-semibold hover:bg-gray-50 transition-colors"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleConfirm}
                        className="flex-1 py-3 bg-blue-600 text-white rounded-xl font-bold hover:bg-blue-700 shadow-lg transition-all active:scale-95"
                    >
                        Confirm
                    </button>
                </div>
            </div>
        </div>
    );
};

export default BookingModal;