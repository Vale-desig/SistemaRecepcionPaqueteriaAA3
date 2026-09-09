// Componente encargado de mostrar la lista de usuarios registrados.
function TablaUsuarios({ usuarios, onEditar, onEliminar }) {
    return (
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
                    {usuarios.length === 0 ? (
                        <tr>
                            <td colSpan="8">
                                No hay usuarios registrados.
                            </td>
                        </tr>
                    ) : (
                        usuarios.map((usuario) => (
                            <tr key={usuario.idUsuario}>
                                <td>{usuario.idUsuario}</td>
                                <td>{usuario.usuaNombre}</td>
                                <td>{usuario.usuaCedula}</td>
                                <td>{usuario.usuaTelefono}</td>
                                <td>{usuario.usuaCorreo}</td>
                                <td>{usuario.usuaApartamento}</td>
                                <td>{usuario.idRol}</td>

                                <td>
                                    <button
                                    type="button"
                                    onClick={() => onEditar(usuario)}
                                    >
                                        Editar
                                    </button>

                                    <button
                                    type="button"
                                    onClick={() => onEliminar(usuario)}
                                    >
                                        Eliminar
                                    </button>
                                </td>
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
        </div>
    )
}

export default TablaUsuarios