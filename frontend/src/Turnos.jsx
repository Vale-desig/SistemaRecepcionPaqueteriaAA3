import { useEffect, useState } from "react"

const API_URL = "http://localhost:8080/api/turnos"

function Turnos(){
    const [turnos, setTurnos] = useState([])
    const [cargando, setCargando] = useState(true)
    const [mensaje, setMensaje] = useState("")
    const [guardando, setGuardando] = useState(null)

    const obtenerHeaders = () => ({
        "Content-Type": "application/json",
        "Authorization": `Bearer ${sessionStorage.getItem("token")}`
    })

    const cargarTurnos = async () => {
        try {
            const respuesta = await fetch(API_URL, {
                headers: obtenerHeaders()
            })

            if (!respuesta.ok) {
                throw new Error("No se pudieron cargar los turnos.")
            }

            const datos = await respuesta.json()
            setTurnos(datos)
        } catch (error) {
            console.error(error)
            setMensaje("No fue posible cargar los turnos.")
        } finally {
            setCargando(false)
        }
    }

    useEffect(() => {
        cargarTurnos()
    }, [])

    const manejarCambio = (id, campo, valor) => {
        setTurnos(turnosAnteriores =>
            turnosAnteriores.map(turno =>
                turno.idTurno === id
                ? { ...turno, [campo]: valor }
                : turno
            )
        )
    }

    const guardarTurno = async (turno) => {
        setMensaje("")
        setGuardando(turno.idTurno)

        try {
            const respuesta = await fetch(
                `${API_URL}/${turno.idTurno}`,
                {
                    method: "PUT",
                    headers: obtenerHeaders(),
                    body: JSON.stringify(turno)
                }
            )

            const datos = await respuesta.json()

            if (!respuesta.ok) {
                throw new Error(datos.mensaje || "No se pudo actualizar el turno.")
            }

            setTurnos(turnosAnteriores =>
                turnosAnteriores.map(actual =>
                    actual.idTurno === turno.idTurno ? datos : actual
                )
            )

            setMensaje(`El turno ${datos.turnoNombre} se actualizó correctamente.`)
        } catch (error) {
            console.error(error)
            setMensaje(error.menssage)
        } finally {
            setGuardando(null)
        }
    }

    if (cargando) {
        return <p>Cargando turnos...</p>
    }

    return (
        <section className="tarjeta">
            <h2>Configuración de turnos</h2>

            <p>
                Desde aquí puedes modificar los nombres y horarios
                de los tres turnos del personal de portería.
            </p>

            {turnos.map(turno => (
                <article className="tarjeta" key={turno.idTurno}>
                    <h3>Turno {turno.idTurno}</h3>

                    <label>
                        Nombre del turno
                        <input
                        type="text"
                        value={turno.turnoNombre}
                        onChange={e =>
                            manejarCambio(
                                turno.idTurno,
                                "turnoNombre",
                                e.target.value
                            )
                        }
                    />
                </label>

                <label>
                    Hora de inicio
                    <input
                    type="time"
                    value={turno.turnoHoraInicio?.substring(0, 5)}
                    onChange={e =>
                        manejarCambio(
                            turno.idTurno,
                            "turnoHoraInicio",
                            `${e.target.value}:00`
                        )
                    }
                    />
                </label>

                <label>
                    Hora de finalización
                    <input
                    type="time"
                    value={turno.turnoHoraFin?.substring(0, 5)}
                    onChange={e =>
                        manejarCambio(
                            turno.idTurno,
                            "turnoHoraFin",
                            `${e.target.value}:00`
                        )
                    }
                    />
                </label>

                <button
                type="button"
                onClick={() => guardarTurno(turno)}
                disabled={guardando !== null}
                >
                    {guardando === turno.idTurno
                    ? "Guardando..."
                    : "Guardar cambios"}
                </button>
                </article>
            ))}

            {mensaje && <p className="mensaje">{mensaje}</p>}
        </section>
    )
}

export default Turnos