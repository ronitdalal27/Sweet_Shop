import { useEffect, useState } from "react"
import { addSweet, deleteSweet, getAllSweets, restockSweet } from "../api/SweetApi"
import SweetCard from "../components/SweetCard"

function AdminPanel() {
  const [sweets, setSweets] = useState([])
  const [form, setForm] = useState({
    name: "",
    category: "",
    price: "",
    quantity: "",
  })

  const load = async () => {
    const res = await getAllSweets()
    setSweets(res.data)
  }

  useEffect(() => {
    load()
  }, [])

  const submit = async (e) => {
    e.preventDefault()
    await addSweet(form)
    setForm({ name: "", category: "", price: "", quantity: "" })
    load()
  }

  return (
    <div className="p-6">
      <h1 className="text-xl font-bold mb-4">Admin Panel</h1>

      <form onSubmit={submit} className="flex gap-2 mb-6">
        {Object.keys(form).map(key => (
          <input
            key={key}
            placeholder={key}
            className="border p-2"
            value={form[key]}
            onChange={e => setForm({ ...form, [key]: e.target.value })}
          />
        ))}
        <button className="bg-green-600 text-white px-4">Add</button>
      </form>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {sweets.map(s => (
          <SweetCard
            key={s.id}
            sweet={s}
            isAdmin={true}
            onDelete={async (id) => { await deleteSweet(id); load() }}
            onRestock={async (id) => { await restockSweet(id, 5); load() }}
          />
        ))}
      </div>
    </div>
  )
}

export default AdminPanel
