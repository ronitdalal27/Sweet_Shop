import { createContext, useContext, useEffect, useState } from "react"
import { jwtDecode } from "jwt-decode"

const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)

  useEffect(() => {
    const token = localStorage.getItem("token")
    if (token) {
      try {
        const decoded = jwtDecode(token)
        setUser({
          username: decoded.sub,
          roles: decoded.roles || [],
        })
      } catch (err) {
        console.error("Invalid token", err)
        localStorage.removeItem("token")
      }
    }
  }, [])

  const logout = () => {
    localStorage.removeItem("token")
    setUser(null)
  }

  const isAdmin = user?.roles?.includes("ROLE_ADMIN")

  return (
    <AuthContext.Provider value={{ user, isAdmin, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
