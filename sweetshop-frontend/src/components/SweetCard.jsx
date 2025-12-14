function SweetCard({ sweet, onPurchase, isAdmin, onDelete, onRestock }) {
  return (
    <div className="border p-4 rounded shadow">
      <h2 className="font-bold">{sweet.name}</h2>
      <p>Category: {sweet.category}</p>
      <p>Price: ₹{sweet.price}</p>
      <p>Stock: {sweet.quantity}</p>

      {!isAdmin && (
        <button
          disabled={sweet.quantity === 0}
          onClick={() => onPurchase(sweet.id)}
          className="mt-2 bg-green-600 text-white px-3 py-1 rounded disabled:bg-gray-400"
        >
          Purchase
        </button>
      )}

      {isAdmin && (
        <div className="flex gap-2 mt-2">
          <button
            onClick={() => onDelete(sweet.id)}
            className="bg-red-600 text-white px-2 py-1 rounded"
          >
            Delete
          </button>

          <button
            onClick={() => onRestock(sweet.id)}
            className="bg-blue-600 text-white px-2 py-1 rounded"
          >
            Restock +5
          </button>
        </div>
      )}
    </div>
  )
}

export default SweetCard
