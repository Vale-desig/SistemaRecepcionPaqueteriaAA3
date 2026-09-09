// Dirección del backend donde se gestionan los usuarios.
const API_URL = "http://localhost:8080/api/usuarios"

// Obtiene todos los usuarios registrados.
export const obtenerUsuarios = async () => {
    const respuesta = await fetch(API_URL)

    if (!respuesta.ok) {
        throw new Error("No se pudieron obtener los usuarios.")
    }

    return  await respuesta.json()
}

// Registra un nuevo usuario.
export const crearUsuario = async (usuario) => {
    const respuesta = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuario)
    })

    if (!respuesta.ok) {
        throw new Error("No se pudo registrar el usuario.")
    }

    return await respuesta.json()
}

// Actualiza un usuario existente.
export const actualizarUsuario = async (idUsuario, usuario) => {
    const respuesta = await fetch(`${API_URL}/${idUsuario}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuario)
    })

    if (!respuesta.ok) {
        throw new Error("No se pudo actualizar el usuario.")
    }

    return await respuesta.json()
}

// Elimina un usuario según su identificador.
export const eliminarUsuario = async (idUsuario) => {
    const respuesta = await fetch(`${API_URL}/${idUsuario}`, {
        method: "DELETE"
    })

    if (!respuesta.ok) {
        throw new Error("No se pudo eliminar el usuario.")
    }
}