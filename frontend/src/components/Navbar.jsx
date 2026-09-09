// Componente encargado de mostrar el menú lateral del sistema.
import { useState } from "react"

function Navbar({
    vista,
    onInicio,
    onRegistrar,
    onMostrarUsuarios,
    onPaquetes
}) {
    const [menuAbierto, setMenuAbierto] = useState(false)

    const cerrarMenu = () => {
        setMenuAbierto(false)
    }

    return (
        <>
        <button
        type="button"
        className={`boton-menu ${menuAbierto ? "boton-menu-abierto" : ""}`}
        onClick={() => setMenuAbierto(!menuAbierto)}
        >
            ☰
        </button>

        <nav className={`navegacion ${menuAbierto ? "menu-abierto" : ""}`}>
            <button
            type="button"
            className={vista === "inicio" ? "activo" : ""}
            onClick={() => {
                onInicio()
                cerrarMenu()
            }}
            >
                🏠 Inicio
            </button>

            <button
            type="button"
            className={vista === "registrar" ? "activo" : ""}
            onClick={() => {
                onRegistrar()
                cerrarMenu()
            }}
            >
                👤 Registrar usuario
            </button>

            <button
            type="button"
            className={vista === "usuarios" ? "activo" : ""}
            onClick={() => {
                onMostrarUsuarios()
                cerrarMenu()
            }}
            >
                👥 Usuarios registrados
            </button>

            <button
            type="button"
            className={vista === "paquetes" ? "activo" : ""}
            onClick={() => {
                onPaquetes()
                cerrarMenu()
            }}
            >
                📦 Paquetes
            </button>
        </nav>
        </>
    )
}

export default Navbar