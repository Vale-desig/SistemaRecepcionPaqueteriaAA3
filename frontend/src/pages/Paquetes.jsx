
import { useCallback, useEffect, useState } from "react"

const API_PAQUETES = "http://localhost:8080/api/paquetes"
const API_USUARIOS = "http://localhost:8080/api/usuarios"

const formularioInicial = {
  paquFechaLlegada: new Date().toLocaleDateString("en-CA"),
  paquDescripcion: "",
  paquRemitente: "",
  paquEstado: "recibido",
  paquPeso: "",
  paquDanio: "",
  idUsuario: ""
}

function Paquetes({ sesion }) {
  const [paquetes, setPaquetes] = useState([])
  const [usuarios, setUsuarios] = useState([])
  const [busquedaResidente, setBusquedaResidente] = useState("")
  const [formulario, setFormulario] = useState(formularioInicial)
  const [paqueteEditando, setPaqueteEditando] = useState(null)
  const [mensaje, setMensaje] = useState("")
  const [cargando, setCargando] = useState(true)

  const rol = Number(sesion?.idRol)
  const esResidente = rol === 3
  const puedeGestionar = rol === 1 || rol === 2
  const puedeEliminar = rol === 1

  const obtenerHeaders = () => ({
    "Content-Type": "application/json",
    "Authorization": `Bearer ${sessionStorage.getItem("token")}`
  })

  const cargarDatos = useCallback(async () => {
    setCargando(true)

    try {
      const respuestaPaquetes = await fetch(API_PAQUETES, {
        headers: obtenerHeaders()
      })

      if (!respuestaPaquetes.ok) {
        throw new Error("No se pudieron cargar los paquetes.")
      }

      const datosPaquetes = await respuestaPaquetes.json()
      setPaquetes(datosPaquetes)

      if (puedeGestionar) {
        const respuestaUsuarios = await fetch(API_USUARIOS, {
          headers: obtenerHeaders()
        })

        if (!respuestaUsuarios.ok) {
          throw new Error("No se pudieron cargar los residentes.")
        }

        const datosUsuarios = await respuestaUsuarios.json()

        setUsuarios(
          datosUsuarios.filter(
            (usuario) => Number(usuario.idRol) === 3
          )
        )
      } else {
        setUsuarios([])
      }

      setMensaje("")

    } catch (error) {
      console.error("Error al cargar los datos:", error)
      setMensaje("No fue posibe cargar los paquetes o los residentes.")
    } finally {
      setCargando(false)
    }
  }, [puedeGestionar])

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    cargarDatos()
  }, [cargarDatos])

  const manejarCambio = (evento) => {
    const { name, value } = evento.target

    setFormulario((anterior) => ({
      ...anterior,
      [name]: value
    }))
  }

  const residentesFiltrados = usuarios.filter((usuario) => {
    const termino = busquedaResidente.trim().toLowerCase()

    return (
      usuario.usuaNombre?.toLowerCase().includes(termino) ||
      usuario.usuaCedula?.toLowerCase().includes(termino) ||
      usuario.usuaApartamento?.toLowerCase().includes(termino)
    )
  })

  const paquetesVisibles = esResidente
    ? paquetes.filter(
        (paquete) => Number(paquete.idUsuario) === Number(sesion.idUsuario)
      )
    : paquetes

  const limpiarFormulario = () => {
    setFormulario({
      ...formularioInicial,
      paquFechaLlegada: new Date().toLocaleDateString("en-CA")
    })
    setPaqueteEditando(null)
    setBusquedaResidente("")
  }

  const guardarPaquete = async (evento) => {
    evento.preventDefault()
    setMensaje("")

    if (!puedeGestionar) return

    if (!formulario.idUsuario) {
      setMensaje("Debes seleccionar un residente.")
      return
    }

    const datosPaquete = {
      paquFechaLlegada: formulario.paquFechaLlegada,
      paquDescripcion: formulario.paquDescripcion.trim(),
      paquRemitente: formulario.paquRemitente.trim(),
      paquEstado: formulario.paquEstado,
      paquPeso: formulario.paquPeso === ""
        ? null
        : Number(formulario.paquPeso),
      paquDanio: formulario.paquDanio.trim() || null,
      idUsuario: Number(formulario.idUsuario)
    }

    try {
      const respuesta = await fetch(
        paqueteEditando
          ? `${API_PAQUETES}/${paqueteEditando}`
          : API_PAQUETES,
        {
          method: paqueteEditando ? "PUT" : "POST",
          headers: obtenerHeaders(),
          body: JSON.stringify(datosPaquete)
        }
      )

      if (!respuesta.ok) {
        throw new Error("No se pudo guardar el paquete.")
      }

      setMensaje(
        paqueteEditando
          ? "Paquete actualizado correctamente."
          : "Paquete registrado correctamente."
      )

      limpiarFormulario()
      await cargarDatos()
    } catch (error) {
      console.error("Error al guardar el paquete:", error)
      setMensaje("No fue posible guardar el paquete.")
    }
  }

  const editarPaquete = (paquete) => {
    if (!puedeGestionar) return

    setFormulario({
      paquFechaLlegada: paquete.paquFechaLlegada || "",
      paquDescripcion: paquete.paquDescripcion || "",
      paquRemitente: paquete.paquRemitente || "",
      paquEstado: paquete.paquEstado || "recibido",
      paquPeso: paquete.paquPeso ?? "",
      paquDanio: paquete.paquDanio || "",
      idUsuario: String(paquete.idUsuario ?? "")
    })

    setPaqueteEditando(paquete.idPaquete)
    setMensaje("")
    window.scrollTo({ top: 0, behavior: "smooth" })
  }

  const eliminarPaquete = async (paquete) => {
    if (!puedeEliminar) return

    const confirmar = window.confirm(
      `¿Seguro que deseas eliminar el paquete "${paquete.paquDescripcion}"?`
    )

    if (!confirmar) return

    try {
      const respuesta = await fetch(
        `${API_PAQUETES}/${paquete.idPaquete}`,
        {
          method: "DELETE",
          headers: obtenerHeaders()
        }
      )

      if (!respuesta.ok) {
        throw new Error("No se pudo eliminar el paquete.")
      }

      setMensaje("Paquete eliminado correctamente.")
      await cargarDatos()
    } catch (error) {
      console.error("Error al eliminar el paquete:", error)
      setMensaje("No fue posible eliminar el paquete.")
    }
  }

  const obtenerResidente = (idUsuario) => {
    return usuarios.find(
      (usuario) => Number(usuario.idUsuario) === Number(idUsuario)
    )
  }

  return (
    <section className="tarjeta">
      <h2>{esResidente ? "Mis paquetes" : "Gestión de paquetes"}</h2>

      {puedeGestionar && (
        <form className="formulario" onSubmit={guardarPaquete}>
          <h3>
            {paqueteEditando ? "Editar paquete" : "Registrar paquete"}
          </h3>

          <label htmlFor="paquFechaLlegada">Fecha de llegada</label>
          <input
            id="paquFechaLlegada"
            type="date"
            name="paquFechaLlegada"
            value={formulario.paquFechaLlegada}
            onChange={manejarCambio}
            required
          />

          <label htmlFor="paquDescripcion">Descripción del paquete</label>
          <input
            id="paquDescripcion"
            type="text"
            name="paquDescripcion"
            value={formulario.paquDescripcion}
            onChange={manejarCambio}
            placeholder="Ej. Caja de zapatos"
            required
          />

          <label htmlFor="paquRemitente">Remitente</label>
          <input
            id="paquRemitente"
            type="text"
            name="paquRemitente"
            value={formulario.paquRemitente}
            onChange={manejarCambio}
            placeholder="Nombre de quien envía el paquete"
            required
          />

          <label htmlFor="busquedaResidente">
            Buscar residente por nombre, cédula o apartamento
          </label>
          <input
            id="busquedaResidente"
            type="text"
            value={busquedaResidente}
            onChange={(evento) => setBusquedaResidente(evento.target.value)}
            placeholder="Escribe un nombre, cédula o apartamento..."
          />

          <label htmlFor="idUsuario">Residente destinatario</label>
          <select
            id="idUsuario"
            name="idUsuario"
            value={formulario.idUsuario}
            onChange={manejarCambio}
            required
          >
            <option value="">Selecciona un residente</option>

            {residentesFiltrados.map((usuario) => (
              <option key={usuario.idUsuario} value={usuario.idUsuario}>
                {usuario.usuaNombre} - Cédula: {usuario.usuaCedula}
                {" - Apto: "}
                {usuario.usuaApartamento || "Sin apartamento"}
              </option>
            ))}
          </select>

          {busquedaResidente && residentesFiltrados.length === 0 && (
            <p>No se encontraron residentes con esa búsqueda.</p>
          )}

          <label htmlFor="paquPeso">Peso en kilogramos (opcional)</label>
          <input
            id="paquPeso"
            type="number"
            name="paquPeso"
            value={formulario.paquPeso}
            onChange={manejarCambio}
            min="0.01"
            step="0.01"
            placeholder="Ej. 1.50"
          />

          <label htmlFor="paquDanio">Daños observados (si los hay)</label>
          <input
            id="paquDanio"
            type="text"
            name="paquDanio"
            value={formulario.paquDanio}
            onChange={manejarCambio}
            placeholder="Describe el daño o déjalo vacío"
          />

          <div className="botones">
            <button type="submit">
              {paqueteEditando ? "Guardar cambios" : "Registrar paquete"}
            </button>

            {paqueteEditando && (
              <button type="button" onClick={limpiarFormulario}>
                Cancelar
              </button>
            )}
          </div>
        </form>
      )}

      {mensaje && <p className="mensaje">{mensaje}</p>}

      <hr />

      <h3>{esResidente ? "Mis paquetes registrados" : "Paquetes registrados"}</h3>

      {cargando ? (
        <p>Cargando paquetes...</p>
      ) : paquetesVisibles.length === 0 ? (
        <p>No hay paquetes registrados.</p>
      ) : (
        <div className="tabla-contenedor">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Fecha</th>
                <th>Descripción</th>
                <th>Remitente</th>
                <th>Residente</th>
                <th>Estado</th>
                <th>Peso (kg)</th>
                <th>Daños</th>
                {puedeGestionar && <th>Acciones</th>}
              </tr>
            </thead>

            <tbody>
              {paquetesVisibles.map((paquete) => {
                const residente = obtenerResidente(paquete.idUsuario)

                return (
                  <tr key={paquete.idPaquete}>
                    <td>{paquete.idPaquete}</td>
                    <td>{paquete.paquFechaLlegada}</td>
                    <td>{paquete.paquDescripcion}</td>
                    <td>{paquete.paquRemitente}</td>
                    <td>
                      {residente
                        ? `${residente.usuaNombre} (${residente.usuaCedula})`
                        : Number(paquete.idUsuario) === Number(sesion.idUsuario)
                          ? sesion.nombre
                          : `Usuario ID: ${paquete.idUsuario}`}
                    </td>
                    <td>{paquete.paquEstado}</td>
                    <td>{paquete.paquPeso ?? "—"}</td>
                    <td>{paquete.paquDanio || "Ninguno"}</td>

                    {puedeGestionar && (
                      <td>
                        <button
                          type="button"
                          onClick={() => editarPaquete(paquete)}
                        >
                          Editar
                        </button>

                        {puedeEliminar && (
                          <button
                          type="button"
                          onClick={() => eliminarPaquete(paquete)}
                          >
                            Eliminar
                          </button>
                        )}
                      </td>
                    )}
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}
    </section>
  )
}

export default Paquetes