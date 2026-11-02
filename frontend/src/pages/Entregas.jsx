import { useCallback, useEffect, useState } from "react"

const API_URL = "http://localhost:8080/api";

function Entregas() {
    const [paquetes, setPaquetes] = useState([]);
    const [entregas, setEntregas] = useState([]);
    const [idPaquete, setIdPaquete] = useState("");
    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");
    const [cargando, setCargando] = useState(false);

    const obtenerHeaders = () => ({
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`,
    });

    const cargarDatos = useCallback(async () => {
        try {
            setError("");

            const [respuestaPaquetes, respuestaEntregas] = await Promise.all([
                fetch(`${API_URL}/paquetes`, {
                    headers: obtenerHeaders(),
                }),
                fetch(`${API_URL}/entregas`, {
                    headers: obtenerHeaders(),
                }),
            ]);

            if (!respuestaPaquetes.ok || !respuestaEntregas.ok) {
                throw new Error("No fue posible cargar la información.");
            }

            const datosPaquetes = await respuestaPaquetes.json();
            const datosEntregas = await respuestaEntregas.json();

            setPaquetes(datosPaquetes);
            setEntregas(datosEntregas);
        } catch (err) {
            setError(err.message);
        }
    }, []);

    useEffect(() => {
        // eslint-disable-next-line react-hooks/set-state-in-effect
        cargarDatos();
    }, [cargarDatos]);

    const paquetesDisponibles = paquetes.filter((paquete) => {
        const yaEntregado = entregas.some(
            (entrega) => Number(entrega.idPaquete) === Number(paquete.idPaquete)
        );

        return (
            paquete.paquEstado?.toLowerCase() === "recibido" && !yaEntregado
        );
    });

    const registrarEntrega = async (evento) => {
        evento.preventDefault();

        if (!idPaquete) {
            setError("Seleccione un paquete para continuar.");
            return;
        }

        try {
            setCargando(true);
            setError("");
            setMensaje("");

            const respuesta = await fetch(`${API_URL}/entregas`, {
                method: "POST",
                headers: obtenerHeaders(),
                body: JSON.stringify({
                    idPaquete: Number(idPaquete),
                }),
            });

            const datos = await respuesta.json();

            if (!respuesta.ok) {
                throw new Error(datos.mensaje || "No se pudo registrar la entrega.");
            }

            setMensaje("La entrega se registró correctamente.");
            setIdPaquete("");
            await cargarDatos();
        } catch (err) {
            setError(err.message);
        } finally {
            setCargando(false);
        }
    };

    return (
        <section className="contenedor-paquetes">
            <h2>Entrega de paquetes</h2>

            <p>
                Registre la entrega de los paquetes que se encuentran en estado
                recibido
            </p>

            {mensaje && <p className="mensaje-exito">{mensaje}</p>}
            {error && <p className="mensaje-error">{error}</p>}

            <form onSubmit={registrarEntrega}>
                <label htmlFor="paqueteEntrega">Seleccione el paquete:</label>

                <select
                id="paqueteEntrega"
                value={idPaquete}
                onChange={(evento) => setIdPaquete(evento.target.value)}
                required
                >
                    <option value="">Seleccione un paquete</option>

                    {paquetesDisponibles.map((paquete) => (
                        <option key={paquete.idPaquete} value={paquete.idPaquete}>
                            Paquete #{paquete.idPaquete} - {paquete.paquDescripcion}
                        </option>
                    ))}
                </select>

                <button type="submit" disabled={cargando || paquetesDisponibles.length === 0}>
                    {cargando ? "Registrando..." : "Registrar entrega"}
                </button>
            </form>

            {paquetesDisponibles.length === 0 && (
                <p>No hay paquetes pendientes de entrega.</p>
            )}

            <h3>Entregas registradas</h3>

            <div className="tabla-contenedor">
                <table>
                    <thead>
                        <tr>
                            <th>N.º entrega</th>
                            <th>Paquete</th>
                            <th>Fecha</th>
                            <th>Hora</th>
                            <th>Portero</th>
                            <th>Confirmación</th>
                        </tr>
                    </thead>

                    <tbody>
                        {entregas.map((entrega) => (
                            <tr key={entrega.idEntrega}>
                                <td>{entrega.idEntrega}</td>
                                <td>{entrega.idPaquete}</td>
                                <td>{entrega.entrFechaEntrega}</td>
                                <td>{entrega.entrHoraEntrega}</td>
                                <td>{entrega.idPortero}</td>
                                <td>{entrega.entrConfirmacion}</td>
                            </tr>
                        ))}

                        {entregas.length === 0 && (
                            <tr>
                                <td colSpan="6">Todavía no hay entregas registradas.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </section>
    );
}

export default Entregas;