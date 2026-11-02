
import { useCallback, useState } from "react"
import "./App.css"

import FormularioUsuario from "./components/FormularioUsuario"
import TablaUsuarios from "./components/TablaUsuarios"
import ModalConfirmacion from "./components/ModalConfirmacion"
import Navbar from "./components/Navbar"

import Inicio from "./pages/Inicio"
import Paquetes from "./pages/Paquetes"
import Entregas from "./pages/Entregas"
import Login from "./pages/Login"
import Incidentes from "./pages/Incidentes"

const API_URL = "http://localhost:8080/api/usuarios"

function App() {
  const [sesion, setSesion] = useState(() => {
    const datosGuardados = sessionStorage.getItem("sesion")
    return datosGuardados ? JSON.parse(datosGuardados) : null
  })

  const [usuarios, setUsuarios] = useState([])
  const [usuario, setUsuario] = useState({
    usuaNombre: "",
    usuaCedula: "",
    usuaTelefono: "",
    usuaCorreo: "",
    usuaApartamento: "",
    idRol: "",
    usuaContrasena: ""
  })

  const [usuarioEditando, setUsuarioEditando] = useState(null)
  const [mensaje, setMensaje] = useState("")
  const [vista, setVista] = useState("inicio")
  const [usuarioAEliminar, setUsuarioAEliminar] = useState(null)

  const obtenerHeaders = () => ({
    "Content-Type": "application/json",
    "Authorization": `Bearer ${sessionStorage.getItem("token")}`
  })

  const cargarUsuarios = useCallback(async () => {
    try {
      const respuesta = await fetch(API_URL, {
        headers: obtenerHeaders()
      })

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

  const manejarCambio = (evento) => {
    const { name, value } = evento.target

    setUsuario((usuarioAnterior) => {
      const usuarioActualizado = {
        ...usuarioAnterior,
        [name]: value
      }

      if (name === "idRol" && Number(value) === 3) {
        usuarioActualizado.usuaContrasena = ""
      }

      return usuarioActualizado
    })
  }

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
        headers: obtenerHeaders(),
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

  const editarUsuario = (usuarioSeleccionado) => {
    setUsuario({
      usuaNombre: usuarioSeleccionado.usuaNombre || "",
      usuaCedula: usuarioSeleccionado.usuaCedula || "",
      usuaTelefono: usuarioSeleccionado.usuaTelefono || "",
      usuaCorreo: usuarioSeleccionado.usuaCorreo || "",
      usuaApartamento: usuarioSeleccionado.usuaApartamento || "",
      idRol: usuarioSeleccionado.idRol ?? "",
      usuaContrasena: ""
    })

    setUsuarioEditando(usuarioSeleccionado.idUsuario)
    setMensaje("")
    setVista("registrar")
  }

  const eliminarUsuario = (usuarioSeleccionado) => {
    setUsuarioAEliminar(usuarioSeleccionado)
  }

  const confirmarEliminacion = async () => {
    if (!usuarioAEliminar) return

    try {
      const respuesta = await fetch(
        `${API_URL}/${usuarioAEliminar.idUsuario}`,
        {
          method: "DELETE",
          headers: obtenerHeaders()
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
      setMensaje("No fue posible eliminar el usuario.")
    }
  }

  const limpiarFormulario = () => {
    setUsuario({
      usuaNombre: "",
      usuaCedula: "",
      usuaTelefono: "",
      usuaCorreo: "",
      usuaApartamento: "",
      idRol: "",
      usuaContrasena: ""
    })

    setUsuarioEditando(null)
  }

  const mostrarRegistro = () => {
    limpiarFormulario()
    setMensaje("")
    setVista("registrar")
  }

  const mostrarUsuarios = async () => {
    setMensaje("")
    await cargarUsuarios()
    setVista("usuarios")
  }

  const mostrarInicio = () => {
    setMensaje("")
    setVista("inicio")
  }

  const mostrarPaquetes = () => {
    setMensaje("")
    setVista("paquetes")
  }

  const mostrarEntregas = () => {
    setMensaje("")
    setVista("entregas")
  }

  const mostrarIncidentes = () => setVista("incidentes")

  const iniciarSesion = (datos) => {
    sessionStorage.setItem("sesion", JSON.stringify(datos))
    setSesion(datos)
    setVista("inicio")
  }

  const cerrarSesion = () => {
    sessionStorage.removeItem("token")
    sessionStorage.removeItem("sesion")
    setSesion(null)
    setVista("inicio")
    limpiarFormulario()
    setUsuarios([])
    setMensaje("")
  }

  if (!sesion) {
    return (
      <main className="contenedor">
        <header className="encabezado">
          <h1>Sistema de Recepción de Paquetería</h1>
        </header>

        <Login onLogin={iniciarSesion} />
      </main>
    )
  }

  const rol = Number(sesion.idRol)

  return (
    <main className="contenedor">
      <header className="encabezado">
        <h1>Sistema de Recepción de Paquetería</h1>
        <p>Bienvenido, {sesion.nombre}</p>

        <Navbar
          vista={vista}
          rol={rol}
          onInicio={mostrarInicio}
          onRegistrar={mostrarRegistro}
          onMostrarUsuarios={mostrarUsuarios}
          onPaquetes={mostrarPaquetes}
          onEntregas={mostrarEntregas}
          onIncidentes={mostrarIncidentes}
        />

        <button type="button" onClick={cerrarSesion}>
          Cerrar sesión
        </button>
      </header>

      {vista === "inicio" && <Inicio />}

      {vista === "paquetes" && (
        <Paquetes sesion={sesion} />
      )}

      {vista === "incidentes" && (
        <Incidentes sesion={sesion} />
      )}

      {(rol === 1 || rol === 2) && vista === "entregas" && (
        <Entregas />
      )}

      {rol === 1 && vista === "registrar" && (
        <section className="tarjeta">
          <h2>
            {usuarioEditando ? "Editar usuario" : "Registrar usuario"}
          </h2>

          <FormularioUsuario
            usuario={usuario}
            usuarioEditando={usuarioEditando}
            onCambio={manejarCambio}
            onGuardar={guardarUsuario}
            onCancelar={limpiarFormulario}
          />

          {mensaje && <p className="mensaje">{mensaje}</p>}
        </section>
      )}

      {rol === 1 && vista === "usuarios" && (
        <section className="tarjeta">
          <h2>Usuarios registrados</h2>

          <TablaUsuarios
            usuarios={usuarios}
            onEditar={editarUsuario}
            onEliminar={eliminarUsuario}
          />

          {mensaje && <p className="mensaje">{mensaje}</p>}
        </section>
      )}

      {rol === 1 && (
        <ModalConfirmacion
          usuario={usuarioAEliminar}
          onConfirmar={confirmarEliminacion}
          onCancelar={() => setUsuarioAEliminar(null)}
        />
      )}
    </main>
  )
}

export default App