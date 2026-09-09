// Componente que muestra una ventana para confirmar la eliminación de un usuario.
function ModalConfirmacion({
    usuario,
    onConfirmar,
    onCancelar
}) {
    if (!usuario) {
        return null
    }

    return (
        <div className="modal-fondo">
            <div className="modal-confirmacion">
                <h2>Confirmar eliminación</h2>

                <p>
                    ¿Está seguro de eliminar al usuario{" "}
                    <strong>{usuario.usuaNombre}</strong>
                </p>

                <div className="modal-botones">
                    <button
                    type="button"
                    className="boton-cancelar"
                    onClick={onCancelar}
                    >
                        Cancelar
                    </button>

                    <button
                    type="button"
                    className="boton-eliminar"
                    onClick={onConfirmar}
                    >
                        Sí, eliminar
                    </button>
                </div>
            </div>
        </div>
    )
}

export default ModalConfirmacion