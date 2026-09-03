import { useCallback, useState } from "react"
import "./App.css"

const API_URL = "http://localhost:8080/api/usuarios"

function App() {
  const [usuarios, setUsuarios] = useState([])
  const [usuario, setUsuario] = useState({
    usuaNombre: "",
    usuaCedula: "",
    usuaTelefono: "",
    usuaCorreo: "",
    usuaApartamento: "",
    idRol: ""
  })

  const [usuarioEditando, setUsuarioEditando] = useState(null)
  const [mensaje, setMensaje] = useState("")
  const [vista, setVista] = useState("registrar")
  const [usuarioAEliminar, setUsuarioAEliminar] = useState(null)

  // Consulta todos los usuarios registrados en el backend.
  const cargarUsuarios = useCallback(async () => {
    try {
      const respuesta = await fetch(API_URL)

      if (!respuesta.ok) {
        throw new Error("No se pudieron obtener los usuarios.")
      }

      const datos = await respuesta.json()
      setUsuarios(datos)
    } catch (error) {
      console.error("Error al cargar usuarios:", error)
      setMensaje("No fue posible cargar los usuarios.")
    }
  }, [])

  // Actualiza los valores del formulario.
  const manejarCambio = (evento) => {
    const { name, value } = evento.target

    setUsuario({
      ...usuario,
      [name]: value
    })
  }

  // Registra un nuevo usuario o actualiza uno existente.
  const guardarUsuario = async (evento) => {
    evento.preventDefault()

    const datosUsuario = {
      ...usuario,
      idRol: Number(usuario.idRol)
    }

    try {
      const url = usuarioEditando
        ? `${API_URL}/${usuarioEditando}`
        : API_URL

      const metodo = usuarioEditando ? "PUT" : "POST"

      const respuesta = await fetch(url, {
        method: metodo,
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(datosUsuario)
      })

      if (!respuesta.ok) {
        throw new Error("No se pudo guardar el usuario.")
      }

      setMensaje(
        usuarioEditando
          ? "Usuario actualizado correctamente."
          : "Usuario registrado correctamente."
      )

      limpiarFormulario()
      await cargarUsuarios()
    } catch (error) {
      console.error("Error al guardar usuario:", error)
      setMensaje("No fue posible guardar el usuario.")
    }
  }

  // Carga los datos del usuario seleccionado en el formulario.
  const editarUsuario = (usuarioSeleccionado) => {
    setUsuario({
      usuaNombre: usuarioSeleccionado.usuaNombre || "",
      usuaCedula: usuarioSeleccionado.usuaCedula || "",
      usuaTelefono: usuarioSeleccionado.usuaTelefono || "",
      usuaCorreo: usuarioSeleccionado.usuaCorreo || "",
      usuaApartamento: usuarioSeleccionado.usuaApartamento || "",
      idRol: usuarioSeleccionado.idRol || ""
    })

    setUsuarioEditando(usuarioSeleccionado.idUsuario)
    setMensaje("")
    setVista("registrar")
  }

  // Abre la ventana de confirmación para eliminar un usuario.
  const eliminarUsuario = (usuarioSeleccionado) => {
    setUsuarioAEliminar(usuarioSeleccionado)
  }


  // Elimina el usuario después de confirmar.
  const confirmarEliminacion = async () => {
    if (!usuarioAEliminar) {
      return
    }

    try {
      const respuesta = await fetch(
        `${API_URL}/${usuarioAEliminar.idUsuario}`,
        {
          method: "DELETE"
        }
      )

      if (!respuesta.ok) {
        throw new Error("No se pudo eliminar el usuario.")
      }

      setMensaje("Usuario eliminado correctamente.")
      setUsuarioAEliminar(null)

      await cargarUsuarios()
    } catch (error) {
      console.error("Error al eliminar usuarios:", error)
      setMensaje("NO fue posible eliminar el usuario.")
    }
  }

  // Limpia el formulario y cancela el modo de edición.
  const limpiarFormulario = () => {
    setUsuario({
      usuaNombre: "",
      usuaCedula: "",
      usuaTelefono: "",
      usuaCorreo: "",
      usuaApartamento: "",
      idRol: ""
    })

    setUsuarioEditando(null)
  }

  // Cambia a la pantalla de registro.
  const mostrarRegistro = () => {
    limpiarFormulario()
    setMensaje("")
    setVista("registrar")
  }

  // Cambia a la pantalla de usuarios registrados.
  const mostrarUsuarios = async () => {
    setMensaje("")
    await cargarUsuarios()
    setVista("usuarios")
  }

  return (
    <main className="contenedor">
      <header className="encabezado">
        <h1>Sistema de Recepción de Paquetería</h1>
        <p>Gestión de usuarios</p>

        <nav className="navegacion">
          <button
            type="button"
            className={vista === "registrar" ? "activo" : ""}
            onClick={mostrarRegistro}
          >
            Registrar usuario
          </button>

          <button
            type="button"
            className={vista === "usuarios" ? "activo" : ""}
            onClick={mostrarUsuarios}
          >
            Usuarios registrados
          </button>
        </nav>
      </header>

      {vista === "registrar" && (
        <section className="tarjeta">
          <h2>
            {usuarioEditando ? "Editar usuario" : "Registrar usuario"}
          </h2>

          <form onSubmit={guardarUsuario} className="formulario">
            <label>
              Nombre
              <input
                type="text"
                name="usuaNombre"
                value={usuario.usuaNombre}
                onChange={manejarCambio}
                pattern="[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+"
                title="El nombre solo debe tener letras y espacios."
                maxLength="45"
                required
              />
            </label>

            <label>
              Cédula
              <input
                type="text"
                name="usuaCedula"
                value={usuario.usuaCedula}
                onChange={manejarCambio}
                inputMode="numeric"
                pattern="[0-9]+"
                title="La cédula solo debe contener números."
                maxLength="45"
                required
              />
            </label>

            <label>
              Teléfono
              <input
                type="text"
                name="usuaTelefono"
                value={usuario.usuaTelefono}
                onChange={manejarCambio}
                inputMode="numeric"
                pattern="[0-9]+"
                title="El teléfono solo debe contener números."
                maxLength="20"
                required
              />
            </label>

            <label>
              Correo
              <input
                type="email"
                name="usuaCorreo"
                value={usuario.usuaCorreo}
                onChange={manejarCambio}
                maxLength="100"
                required
              />
            </label>

            <label>
              Apartamento
              <input
                type="text"
                name="usuaApartamento"
                value={usuario.usuaApartamento}
                onChange={manejarCambio}
                pattern="[A-Za-zÁÉÍÓÚáéíóúÑñÜü0-9 \-]+"
                title="El apartamento solo debe contener letras, números, espacios y guiones."
                maxLength="45"
                required
              />
            </label>

            <label>
              Rol
              <select
                name="idRol"
                value={usuario.idRol}
                onChange={manejarCambio}
                required
              >
                <option value="">Seleccione un rol</option>
                <option value="1">Administrador</option>
                <option value="2">Portero</option>
                <option value="3">Residente</option>
              </select>
            </label>

            <div className="botones">
              <button type="submit">
                {usuarioEditando
                  ? "Actualizar usuario"
                  : "Guardar usuario"}
              </button>

              {usuarioEditando && (
                <button
                  type="button"
                  onClick={limpiarFormulario}
                >
                  Cancelar
                </button>
              )}
            </div>
          </form>

          {mensaje && <p className="mensaje">{mensaje}</p>}
        </section>
      )}

      {vista === "usuarios" && (
        <section className="tarjeta">
          <h2>Usuarios registrados</h2>

          {usuarios.length === 0 ? (
            <p>No hay usuarios registrados.</p>
          ) : (
            <div className="tabla-contenedor">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Cédula</th>
                    <th>Teléfono</th>
                    <th>Correo</th>
                    <th>Apartamento</th>
                    <th>Rol</th>
                    <th>Acciones</th>
                  </tr>
                </thead>

                <tbody>
                  {usuarios.map((usuarioItem) => (
                    <tr key={usuarioItem.idUsuario}>
                      <td>{usuarioItem.idUsuario}</td>
                      <td>{usuarioItem.usuaNombre}</td>
                      <td>{usuarioItem.usuaCedula}</td>
                      <td>{usuarioItem.usuaTelefono}</td>
                      <td>{usuarioItem.usuaCorreo}</td>
                      <td>{usuarioItem.usuaApartamento}</td>
                      <td>{usuarioItem.idRol}</td>

                      <td>
                        <div className="acciones">
                          <button
                            type="button"
                            onClick={() =>
                              editarUsuario(usuarioItem)
                            }
                          >
                            Editar
                          </button>

                          <button
                          type="button"
                          onClick={() => eliminarUsuario(usuarioItem)}
                          >
                            Eliminar
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {mensaje && <p className="mensaje">{mensaje}</p>}
        </section>
      )}

      {usuarioAEliminar && (
        <div className="modal-fondo">
          <div className="modal-confirmacion">
            <h2>¿Eliminar usuario?</h2>

            <p>
              ¿Está seguro de que desea eliminar a{" "}
              <strong>{usuarioAEliminar.usuaNombre}</strong>?
            </p>

            <div className="modal-botones">
              <button
              type="button"
              className="boton-cancelar"
              onClick={() => setUsuarioAEliminar(null)}
              >
                Cancelar
              </button>

              <button
              type="button"
              className="boton-eliminar"
              onClick={confirmarEliminacion}
              >
                Sí, eliminar
              </button>
            </div>
          </div>
        </div>
      )}
    </main>
  )
}

export default App