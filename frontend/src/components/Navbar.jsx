import { useState } from "react"

function Navbar({
    vista,
    rol,
    onInicio,
    onRegistrar,
    onMostrarUsuarios,
    onPaquetes,
    onEntregas,
    onIncidentes
}) {
    const [menuAbierto, setMenuAbierto] = useState(false)

    const cerrarMenu = () => {
        setMenuAbierto(false)
    }

    return (
        <>
        <button
        type="button"
        className={`boton-menu ${menuAbierto ? "boton-menu--abierto" : ""}`}
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

            {Number(rol) === 1 && (
                <>
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
                </>
            )}

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

            {(Number(rol) === 1 || Number(rol) === 2) && (
                <button
                type="button"
                className={vista === "entregas" ? "activo" : ""}
                onClick={() => {
                    onEntregas()
                    cerrarMenu()
                }}
                >
                    📬 Entregas
                </button>
            )}

            {(Number(rol) === 1 || Number(rol) === 2) && (
                <button
                type="button"
                className={vista === "incidentes" ? "activo" : ""}
                onClick={() => {
                    onIncidentes()
                    cerrarMenu()
                }}
                >
                    ⚠️ Incidentes
                </button>
            )}
        </nav>
        </>
    )
}

export default Navbar