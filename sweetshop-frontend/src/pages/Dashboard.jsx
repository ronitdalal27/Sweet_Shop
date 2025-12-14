import { useEffect, useState } from "react"
import {
  getAllSweets,
  purchaseSweet,
  deleteSweet,
  restockSweet,
  searchSweets,
} from "../api/SweetApi"
import SweetCard from "../components/SweetCard"
import { useAuth } from "../context/AuthContext"

function Dashboard() {
  const [sweets, setSweets] = useState([])
  const { isAdmin } = useAuth()

  const [search, setSearch] = useState({
    name: "",
    category: "",
    minPrice: "",
    maxPrice: "",
  })


  const loadSweets = async () => {
    const res = await getAllSweets()
    setSweets(res.data)
  }

  useEffect(() => {
    loadSweets()
  }, [])


  const handleSearch = async (e) => {
  e.preventDefault()

  const params = {}

  if (search.name.trim()) {
    params.name = search.name.trim()
  }

  if (search.category.trim()) {
    params.category = search.category.trim()
  }

  if (search.minPrice !== "") {
    params.minPrice = Number(search.minPrice)
  }

  if (search.maxPrice !== "") {
    params.maxPrice = Number(search.maxPrice)
  }

  if (Object.keys(params).length === 0) {
    loadSweets()
    return
  }

  const res = await searchSweets(params)
  setSweets(res.data)
}


  const buy = async (id) => {
    await purchaseSweet(id)
    loadSweets()
  }

  const remove = async (id) => {
    await deleteSweet(id)
    loadSweets()
  }

  const restock = async (id) => {
    await restockSweet(id, 5)
    loadSweets()
  }

  return (
    <div className="p-6 bg-gray-100 min-h-screen">
      <h1 className="text-2xl font-bold mb-4">
        Sweet Shop Dashboard
      </h1>

      <form
        onSubmit={handleSearch}
        className="bg-white p-4 rounded shadow mb-6 grid grid-cols-1 md:grid-cols-4 gap-4"
      >
        <input
          placeholder="Search by name"
          value={search.name}
          onChange={(e) => setSearch({ ...search, name: e.target.value })}
          className="border p-2 rounded"
        />

        <input
          placeholder="Category"
          value={search.category}
          onChange={(e) => setSearch({ ...search, category: e.target.value })}
          className="border p-2 rounded"
        />

        <input
          type="number"
          placeholder="Min Price"
          value={search.minPrice}
          onChange={(e) => setSearch({ ...search, minPrice: e.target.value })}
          className="border p-2 rounded"
        />

        <input
          type="number"
          placeholder="Max Price"
          value={search.maxPrice}
          onChange={(e) => setSearch({ ...search, maxPrice: e.target.value })}
          className="border p-2 rounded"
        />

        <button
          type="submit"
          className="md:col-span-4 bg-blue-600 text-white py-2 rounded"
        >
          Search
        </button>
      </form>

      {sweets.length === 0 && (
        <p className="text-gray-500">No sweets available</p>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {sweets.map((sweet) => (
          <SweetCard
            key={sweet.id}
            sweet={sweet}
            isAdmin={isAdmin}
            onPurchase={buy}
            onDelete={remove}
            onRestock={restock}
          />
        ))}
      </div>
    </div>
  )
}

export default Dashboard
