import { useState, useEffect } from "react"

function Incidentes({ sesion }) {

    const [paquetes, setPaquetes] = useState([])
    const [tipo, setTipo] = useState("Dañado")
    const [descripcion, setDescripcion] = useState("")
    const [idPaquete, setIdPaquete] = useState("")
    const [mensaje, setMensaje] = useState("")
    const [guardando, setGuardando] = useState(false)
    const [incidentes, setIncidentes] = useState([])

    const esAdministrador = Number(sesion.idRol) === 1
    const esPortero = Number(sesion.idRol) === 2

    const formatearFechaHora = (fecha, hora) => {
        if (!fecha) return "Sin fecha"

        const [anio, mes, dia] = fecha.split("-")
        const fechaFormateada = `${dia}/${mes}/${anio}`

        if (!hora) return fechaFormateada

        const [horas, minutos] = hora.split(":").map(Number)

        const periodo = hora >= 12 ? "p. m." : "a. m."
        const hora12 = horas % 12 || 12

        return `${fechaFormateada} - ${hora12}:${String(minutos).padStart(2, "0")} ${periodo}`
    }

    const obtenerHeaders = () => ({
        "Content-Type": "application/json",
        "Authorization": `Bearer ${sessionStorage.getItem("token")}`
    })

    const cargarPaquetes = async (actualizarEstado) => {
        try {
            const respuesta = await fetch("http://localhost:8080/api/paquetes", {
                headers: obtenerHeaders(),
                signal: actualizarEstado.signal
            })

            if (!respuesta.ok) {
                throw new Error("No se pudieron cargar los paquetes.")
            }

            const datos = await respuesta.json()
            actualizarEstado.setPaquetes(datos)

        } catch (error) {
            if (error.name !== "AbortError") {
                console.error("Error al cargar paquetes:", error)
                actualizarEstado.setMensaje("No fue posible cargar los paquetes.")
            }
        }
    }

        const cargarIncidentes = async (actualizarEstado) => {
            try {
                const respuesta = await fetch("http://localhost:8080/api/incidentes", {
                    headers: obtenerHeaders(),
                    signal: actualizarEstado.signal
                })

                if (!respuesta.ok) {
                    throw new Error("No se pudieron cargar los incidentes.")
                }

                const datos = await respuesta.json()
                actualizarEstado.setIncidentes(datos)

            } catch (error) {
                if (error.name !== "AbortError") {
                    console.error("Error al cargar incidentes:", error)
                    actualizarEstado.setMensaje("No fue posible cargar los incidentes.")
                }
            }
        }

    useEffect(() => {
        const controlador = new AbortController()

        cargarPaquetes({
            setPaquetes,
            setMensaje,
            signal: controlador.signal
        })

        if (esAdministrador) {
            cargarIncidentes({
                setIncidentes,
                setMensaje,
                signal: controlador.signal
            })
        }

        return () => controlador.abort()
    }, [])

    const registrarIncidente = async (evento) => {
        evento.preventDefault()
        if (guardando) return

        if(!tipo || !descripcion.trim() || !idPaquete) {
            setMensaje("Debe completar todos los campos.")
            return
        }

        try {
            setGuardando(true)

            const respuesta = await fetch("http://localhost:8080/api/incidentes", {
                method: "POST",
                headers: obtenerHeaders(),
                body: JSON.stringify({
                    inciTipo: tipo,
                    inciDescripcion: descripcion,
                    idPaquete: Number(idPaquete)
                })
            })

            const datos = await respuesta.json()

            if (!respuesta.ok) {
                throw new Error(datos.mensaje || "No se pudo registrar el incidente.")
            }

            setMensaje("Incidente registrado correctamente.")
            setDescripcion("")
            setIdPaquete("")
            setTipo("Dañado")

        } catch (error) {
            console.error("Error al registrar incidente:", error)
            setMensaje(error.message)
        }

        finally {
            setGuardando(false)
        }
    }

    return (
        <section>
            <h2>Gestión de incidentes</h2>
            <p>Registro y consulta de incidentes de paquetes.</p>

            {mensaje && <p>{mensaje}</p>}

            {esAdministrador && (
                <div>
                    <h3>Incidentes registrados</h3>

                    {incidentes.length === 0 ? (
                        <p>No hay incidentes registrados.</p>
                    ) : (
                        <div className="tabla-contenedor">
                        <table>
                            <thead>
                                <tr>
                                    <th>N.º</th>
                                    <th>Tipo</th>
                                    <th>Descripción</th>
                                    <th>Fecha</th>
                                    <th>Paquete</th>
                                    <th>Portero</th>
                                </tr>
                            </thead>

                            <tbody>
                                {incidentes.map((incidente) => (
                                    <tr key={incidente.idIncidente}>
                                        <td>{incidente.idIncidente}</td>
                                        <td>{incidente.inciTipo}</td>
                                        <td>{incidente.inciDescripcion}</td>
                                        <td>{formatearFechaHora(incidente.inciFecha, incidente.inciHora)}</td>
                                        <td>{incidente.idPaquete}</td>
                                        <td>{incidente.idPortero}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        </div>
                    )}
                </div>
            )}

            {esPortero && (
            <form onSubmit={registrarIncidente}>
            <label>Tipo de incidente:</label>
            <select
            value={tipo}
            onChange={(e) => setTipo(e.target.value)}
            >
                <option value="Dañado">Dañado</option>
                <option value="Perdido">Perdido</option>
            </select>

            <label>Descripción:</label>
            <textarea
            value={descripcion}
            onChange={(e) => setDescripcion(e.target.value)}
            placeholder="Describe lo sucedido..."
            />

            <label>Paquete afectado:</label>
            <select
            value={idPaquete}
            onChange={(e) => setIdPaquete(e.target.value)}
            >
                <option value="">Seleccione un paquete</option>

                {paquetes.map((paquete) => (
                    <option
                    key={paquete.idPaquete}
                    value={paquete.idPaquete}
                    >
                        Paquete #{paquete.idPaquete} - {paquete.paquDescripcion}
                    </option>
                ))}
            </select>
            <button type="submit" disabled={guardando}>
                {guardando ? "Registrando..." : "Registrar incidente"}
            </button>
            </form>
            )}
        </section>
    )
}

export default Incidentes