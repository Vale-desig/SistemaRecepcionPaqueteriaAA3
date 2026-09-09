import { useCallback, useState } from "react"
import "./App.css"

import FormularioUsuario from "./components/FormularioUsuario"
import TablaUsuarios from "./components/TablaUsuarios"
import ModalConfirmacion from "./components/ModalConfirmacion"
import Navbar from "./components/Navbar"

import Inicio from "./pages/Inicio"
import Paquetes from "./pages/Paquetes"

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

  // Cambia a la página principal.
  const mostrarInicio = () => {
    setMensaje("")
    setVista("inicio")
  }

  // Cambia a la página de paquetes.
  const mostrarPaquetes = () => {
    setMensaje("")
    setVista("paquetes")
  }

  return (
    <main className="contenedor">
      <header className="encabezado">
        <h1>Sistema de Recepción de Paquetería</h1>
        <p>Gestión de usuarios</p>

        <Navbar
        vista={vista}
        onInicio={mostrarInicio}
        onRegistrar={mostrarRegistro}
        onMostrarUsuarios={mostrarUsuarios}
        onPaquetes={mostrarPaquetes}
        />
      </header>

      {vista === "inicio" && <Inicio />}

      {vista === "paquetes" && <Paquetes />}

      {vista === "registrar" && (
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
      
      {vista === "usuarios" && (
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

      <ModalConfirmacion
      usuario={usuarioAEliminar}
      onConfirmar={confirmarEliminacion}
      onCancelar={() => setUsuarioAEliminar(null)}
      />
    </main>
  )
}

export default App