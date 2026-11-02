import { useState } from "react"

const API_URL = "http://localhost:8080/api/auth/login"

function Login({ onLogin, onRecuperar }) {
    const [cedula, setCedula] = useState("")
    const [contrasena, setContrasena] = useState("")
    const [mensaje, setMensaje] = useState("")
    const [cargando, setCargando] = useState(false)

    const iniciarSesion =async (evento) => {
        evento.preventDefault()
        setMensaje("")

        if (!cedula.trim() || !contrasena.trim()) {
            setMensaje("Ingrese su cédula y contraseña.")
            return
        }

        try {
            setCargando(true)

            const respuesta = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type" : "application/json"
                },
                body: JSON.stringify({
                    cedula: cedula.trim(),
                    contrasena
                })
            })

            const datos = await respuesta.json()

            if (!respuesta.ok) {
                throw new Error(
                    datos.mensaje || "Cédula o contraseña incorrecta."
                )
            }

            if (!datos.token) {
                throw new Error("El servidor no devolvió el token de acceso.")
            }

            sessionStorage.setItem("token", datos.token)

            onLogin(datos)

        } catch (error) {
            sessionStorage.removeItem("token")
            setMensaje(error.message || "No fue posible iniciar sesión.")
        } finally {
            setCargando(false)
        }
    }

    return (
        <section className="tarjeta login">
            <h2>Iniciar sesión</h2>

            <form onSubmit={iniciarSesion}>
                <label htmlFor="cedula">
                    Número de cédula
                </label>

                <input
                id="cedula"
                type="text"
                value={cedula}
                onChange={(evento) => setCedula(evento.target.value)}
                placeholder="Ingrese su cédula"
                inputMode="numeric"
                required
                />

                <label htmlFor="contrasena">
                    Contraseña
                </label>

                <input
                id="contrasena"
                type="password"
                value={contrasena}
                onChange={(evento) => setContrasena(evento.target.value)}
                placeholder="Ingrese su contraseña"
                required
                />

                {mensaje && (
                    <p className="mensaje">{mensaje}</p>
                )}

                <button type="submit" disabled={cargando}>
                    {cargando ? "Ingresando..." : "Iniciar sesión"}
                </button>

                {onRecuperar && (
                    <button
                    type="button"
                    onClick={onRecuperar}
                    >
                        Recuperar contraseña
                    </button>
                )}
            </form>
        </section>
    )
}

export default Login