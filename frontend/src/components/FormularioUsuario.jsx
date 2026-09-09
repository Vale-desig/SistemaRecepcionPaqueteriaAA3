// Componente encargado de mostrar el formulario de usuarios.
function FormularioUsuario({
    usuario,
    usuarioEditando,
    onCambio,
    onGuardar,
    onCancelar
}) {
    return (
        <form onSubmit={onGuardar} className="formulario">

            <label>
                Nombre
                <input 
                type="text"
                name="usuaNombre"
                value={usuario.usuaNombre}
                onChange={onCambio}
                placeholder="Ingrese el nombre completo"
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
                onChange={onCambio}
                placeholder="Ingrese el número de cédula"
                inputMode="numeric"
                pattern="[0-9]+"
                title="La cédula solo debe contener número."
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
                onChange={onCambio}
                placeholder="Ingrese el número de teléfono"
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
                onChange={onCambio}
                placeholder="ejemplo@correo.com"
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
                onChange={onCambio}
                placeholder="Ejemplo: Apto 101"
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
                onChange={onCambio}
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
                    {usuarioEditando ? "Actualizar usuario" : "Guardar usuario"}
                </button>

                {usuarioEditando && (
                    <button type="button" onClick={onCancelar}>
                        Cancelar
                    </button>
                )}
            </div>

        </form>
    )
}

export default FormularioUsuario