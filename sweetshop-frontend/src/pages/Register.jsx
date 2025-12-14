import { useState } from "react"
import { registerUser } from "../api/authApi"
import { useNavigate } from "react-router-dom"

function Register() {
  const navigate = useNavigate()

  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    role: "",
  })

  const [errors, setErrors] = useState({})

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const validate = () => {
    const err = {}

    if (!form.username.trim()) {
      err.username = "Username is required"
    }

    const emailRegex =
      /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!form.email.trim()) {
      err.email = "Email is required"
    } else if (!emailRegex.test(form.email)) {
      err.email = "Enter a valid email address"
    }

    if (!form.password) {
      err.password = "Password is required"
    } else if (form.password.length < 6) {
      err.password = "Password must be at least 6 characters"
    }

    setErrors(err)
    return Object.keys(err).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!validate()) return

    try {
      await registerUser(form)
      navigate("/login")
    } catch (err) {
      alert("Registration failed")
    }
  }

  return (
    <div className="h-screen flex items-center justify-center bg-gray-100">
      <form
        className="bg-white p-6 rounded shadow w-80"
        onSubmit={handleSubmit}
      >
        <h2 className="text-xl font-bold mb-4">Register</h2>

        <input
          name="username"
          placeholder="Username"
          onChange={handleChange}
          className="w-full border p-2 mb-1"
        />
        {errors.username && (
          <p className="text-red-500 text-sm mb-2">
            {errors.username}
          </p>
        )}

        <input
          name="email"
          placeholder="Email"
          onChange={handleChange}
          className="w-full border p-2 mb-1"
        />
        {errors.email && (
          <p className="text-red-500 text-sm mb-2">
            {errors.email}
          </p>
        )}

        <input
          name="password"
          type="password"
          placeholder="Password"
          onChange={handleChange}
          className="w-full border p-2 mb-1"
        />
        {errors.password && (
          <p className="text-red-500 text-sm mb-2">
            {errors.password}
          </p>
        )}

        <select
          name="role"
          onChange={handleChange}
          className="w-full border p-2 mb-4"
        >
          <option value="">User (default)</option>
          <option value="ADMIN">Admin</option>
        </select>

        <button className="w-full bg-green-600 text-white py-2 rounded">
          Register
        </button>
      </form>
    </div>
  )
}

export default Register
