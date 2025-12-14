import { useState } from "react"
import { loginUser } from "../api/authApi"
import { useNavigate } from "react-router-dom"

function Login() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: "", password: "" })
  const [error, setError] = useState("")

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      const res = await loginUser(form)
      localStorage.setItem("token", res.data.token)

      // Simple role-based redirect (we'll improve later)
      navigate("/")
    } catch (err) {
      setError("Invalid username or password")
    }
  }

  return (
    <div className="h-screen flex items-center justify-center bg-gray-100">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded shadow w-80"
      >
        <h2 className="text-xl font-bold mb-4">Login</h2>

        {error && <p className="text-red-500 text-sm">{error}</p>}

        <input
          name="username"
          placeholder="Username"
          onChange={handleChange}
          className="w-full border p-2 mb-3"
          required
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          onChange={handleChange}
          className="w-full border p-2 mb-4"
          required
        />

        <button className="w-full bg-blue-600 text-white py-2 rounded">
          Login
        </button>
      </form>
    </div>
  )
}

export default Login
