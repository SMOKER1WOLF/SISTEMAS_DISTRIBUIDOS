<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Pedido</title>

        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/styles_pedidos.css">
    </head>
    <body class="bg-light">

        <div class="container mt-5">
            <h2 class="text-center mb-4">Registrar Nuevo Pedido</h2>

            <%-- Muestra mensajes de éxito o error --%>
            <%
                String mensaje = (String) request.getAttribute("mensaje");
                String error = (String) request.getAttribute("error");
                
                if (mensaje != null) {
            %>
                <div class="alert alert-success"><%= mensaje %></div>
            <%
                }
                if (error != null) {
            %>
                <div class="alert alert-danger"><%= error %></div>
            <%
                }
            %>

            <form action="<%= request.getContextPath() %>/PedidoControlador" method="post" class="card p-4 shadow-sm" novalidate>
                <input type="hidden" name="Op" value="Registrar">
                <input type="hidden" id="montoTotalHidden" name="montoTotal">
                <section class="datosCliente">

                    <!-- Fila 1 -->
                    <div class="fila1">
                        <div class="mb-3 cliente">
                            <label for="idCliente" class="form-label">Cliente</label>
                            <select id="idCliente" name="idCliente" class="form-select" required>
                                <option value="">Seleccione un cliente</option>
                                <%
                                    java.util.List<Entidades.Cliente> listaClientes = (java.util.List<Entidades.Cliente>) request.getAttribute("listaClientes");
                                    if (listaClientes != null) {
                                        for (Entidades.Cliente cliente : listaClientes) {
                                %>
                                    <option 
                                        value="<%= cliente.getIdCliente() %>"
                                        data-nombres="<%= cliente.getNombres() %>"
                                        data-apellidos="<%= cliente.getApellidos() %>"
                                        data-direccion="<%= cliente.getDireccion() %>"
                                        data-telefono="<%= cliente.getTelefono() %>">
                                        <%= cliente.getIdCliente() %> - <%= cliente.getNombres() %> <%= cliente.getApellidos() %>
                                    </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3 nombres">
                            <label for="nombres" class="form-label">Nombres</label>
                            <input type="text" id="nombres" class="form-control" readonly>
                        </div>
                    </div>

                    <!-- Fila 2 -->
                    <div class="fila2">
                        <div class="mb-3 direccion">
                            <label for="direccion" class="form-label">Dirección</label>
                            <input type="text" id="direccion" class="form-control" readonly>
                        </div>

                        <div class="mb-3 telefono">
                            <label for="telefono" class="form-label">Teléfono</label>
                            <input type="text" id="telefono" class="form-control" readonly>
                        </div>
                    </div>

                    <!-- Fila 3 -->
                    <div class="fila3">
                        <div class="mb-3 fecha">
                            <%
                                String fechaVisible = (String) request.getAttribute("fechaVisible");
                                if (fechaVisible == null) fechaVisible = "";
                            %>
                            <label class="form-label">Fecha: <%= fechaVisible %></label>
                            <input type="hidden" name="fechaPedido" value="<%= fechaVisible %>">
                        </div>

                        <div class="mb-3 npedido">
                            <%
                                String codigoPedido = (String) request.getAttribute("codigoPedido");
                                if (codigoPedido == null) codigoPedido = "";
                            %>
                            <label class="form-label">N° pedido: <%= codigoPedido %></label> 
                        </div>
                    </div>

                </section>

                <table class="table" id="tablaItems">
                    <thead>
                        <tr>
                            <th>Acciones</th>
                            <th>Item</th>
                            <th>Descripción</th>
                            <th>Precio</th>
                            <th>Cantidad</th>
                            <th>IGV</th>
                            <th>Sub Total</th>
                        </tr>
                    </thead>

                    <tbody>
                        <!-- Filas dinámicas -->
                    </tbody>

                    <tfoot>
                        <tr>
                            <td colspan="7" class="text-center">
                                <a href="#" class="agregarItem btn btn-primary">+ Nuevo Item</a>
                            </td>
                        </tr>
                    </tfoot>
                </table>

                <section class="d-flex justify-content-end mt-4">
                    <div class="card shadow-sm p-3" style="width: 250px;">
                        <h6 class="text-center mb-3">Resumen</h6>

                        <p class="mb-1">SubTotal: <strong id="subtotalText">0.00</strong></p>
                        <p class="mb-1">IGV: <strong id="igvText">0.00</strong></p>
                        <p class="mb-1">Total: <strong id="totalText">0.00</strong></p>
                    </div>
                </section>

                <div class="modalProducto" id="modalProducto">
                    <div class="modal-content">
                        <h5>Agregar Producto</h5>

                        <div class="mb-3 producto">
                            <label for="idArticulo" class="form-label">Producto</label>
                            <select id="modalIdArticulo" class="form-select">
                                <option value="">Seleccione un producto</option>
                                <%
                                    java.util.List<Entidades.Producto> listaProductos = (java.util.List<Entidades.Producto>) request.getAttribute("listaProductos");
                                    if (listaProductos != null) {
                                        for (Entidades.Producto pro : listaProductos) {
                                %>
                                    <option 
                                        value="<%= pro.getIdArticulo() %>"
                                        data-nombre="<%= pro.getNombre() %>"
                                        data-stock="<%= pro.getCantidad() %>"
                                        data-precio="<%= pro.getPrecio() %>">
                                        <%= pro.getIdArticulo() %> - <%= pro.getNombre() %>
                                    </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3 nombre">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" id="modalnombre" class="form-control" readonly>
                        </div>

                        <div class="mb-3 stock">
                            <label for="stock" class="form-label">Stock</label>
                            <input type="text" id="modalstock" class="form-control" readonly>
                        </div>

                        <div class="mb-3 cantidad">
                            <label for="cantidad" class="form-label">Cantidad</label>
                            <input type="number" id="modalcantidad" class="form-control" min="1" value="1">
                        </div>

                        <div class="mb-3 precio">
                            <label for="precio" class="form-label">Precio</label>
                            <input type="number" id="modalprecio" class="form-control" step="0.01">
                        </div>

                        <div class="acciones">
                            <a href="#" id="btnAgregar" class="btn btn-success">Agregar</a>
                            <a href="#" id="btnCerrar" class="btn btn-secondary">Cerrar</a>
                        </div>
                    </div>
                </div>

                <div class="text-center">
                    <button type="submit" class="btn btn-primary">Registrar Pedido</button>
                    <a href="<%= request.getContextPath() %>/UsuarioControlador?Op=VolverInicio" class="btn btn-secondary">Volver al Menú</a>
                    <a href="${pageContext.request.contextPath}/PedidoControlador?Op=Listar" class="btn btn-secondary" >Listar pedidos</a>
                </div>
            </form>
        </div>

    </body>
</html>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<script>
    function actualizarTotales() {
        console.log("=== ACTUALIZANDO TOTALES ===");

        const tablaItems = document.querySelector('#tablaItems tbody');

        if (!tablaItems) {
            console.error("⚠ ERROR: No se encontró la tabla de items");
            return;
        }

        console.log("Filas en la tabla:", tablaItems.rows.length);

        let granSubTotal = 0;
        let granIgv = 0;

        for (let i = 0; i < tablaItems.rows.length; i++) {
            const row = tablaItems.rows[i];
            const igv = parseFloat(row.cells[5]?.innerText || 0);
            const subTotal = parseFloat(row.cells[6]?.innerText || 0);

            console.log(`Fila ${i}: IGV=${igv}, SubTotal=${subTotal}`);

            granSubTotal += subTotal;
            granIgv += igv;
        }

        const granTotal = granSubTotal + granIgv;

        // Actualizar la interfaz
        document.getElementById("subtotalText").textContent = granSubTotal.toFixed(2);
        document.getElementById("igvText").textContent = granIgv.toFixed(2);
        document.getElementById("totalText").textContent = granTotal.toFixed(2);

        // Actualizar el campo hidden
        document.getElementById("montoTotalHidden").value = granTotal.toFixed(2);

        console.log("TOTALES - Subtotal:", granSubTotal.toFixed(2), "IGV:", granIgv.toFixed(2), "Total:", granTotal.toFixed(2));
        console.log("Número de items: " + tablaItems.rows.length);
    }
</script>

<script>
    $(document).ready(function () {
        $('#idCliente').select2({
            width: '100%',
            placeholder: 'Seleccione un cliente'
        });

        let selectedOption = null;

        $('#idCliente').on('change', function () {
            const option = $(this).find(':selected');
            selectedOption = option;

            if (option.val()) {
                $('#nombres').val(option.data('nombres') + " " + option.data('apellidos'));
                $('#direccion').val(option.data('direccion'));
                $('#telefono').val(option.data('telefono'));
            } else {
                $('#nombres, #direccion, #telefono').val('');
            }
        });

        $(document).on('mouseenter', '.select2-results__option', function () {
            const optionValue = $(this).attr('id')?.split('-').pop();
            const realOption = $('#idCliente').find('option[value="' + optionValue + '"]');

            if (realOption.length) {
                $('#nombres').val(realOption.data('nombres') + " " + realOption.data('apellidos'));
                $('#direccion').val(realOption.data('direccion'));
                $('#telefono').val(realOption.data('telefono'));
            }
        });

        $('#idCliente').on('select2:close', function () {
            if (selectedOption?.val()) {
                $('#nombres').val(selectedOption.data('nombres') + " " + selectedOption.data('apellidos'));
                $('#direccion').val(selectedOption.data('direccion'));
                $('#telefono').val(selectedOption.data('telefono'));
            } else {
                $('#nombres, #direccion, #telefono').val('');
            }
        });
    });
</script>

<script>
    document.addEventListener('DOMContentLoaded', function () {

        const modal = document.getElementById('modalProducto');
        const btnAbrir = document.querySelector('.agregarItem');
        const btnCerrar = document.getElementById('btnCerrar');
        const selectProducto = document.getElementById('modalIdArticulo');

        btnAbrir.addEventListener('click', (e) => {
            e.preventDefault();
            modal.style.display = 'flex';
        });

        btnCerrar.addEventListener('click', (e) => {
            e.preventDefault();
            modal.style.display = 'none';
        });

        window.addEventListener('click', (e) => {
            if (e.target === modal)
                modal.style.display = 'none';
        });

        selectProducto.addEventListener('change', function () {
            const selected = this.options[this.selectedIndex];
            document.getElementById("modalnombre").value = selected.getAttribute("data-nombre") || "";
            document.getElementById("modalstock").value = selected.getAttribute("data-stock") || "";
            document.getElementById("modalprecio").value = selected.getAttribute("data-precio") || "";
        });

        const btnAgregar = document.getElementById('btnAgregar');
        const tablaItems = document.getElementById('tablaItems').getElementsByTagName('tbody')[0];
        let itemCounter = 1;
        const IGV_RATE = 0.18;

        btnAgregar.addEventListener('click', function (e) {
            e.preventDefault();

            const selectedOption = selectProducto.options[selectProducto.selectedIndex];

            if (!selectedOption.value) {
                alert('Por favor, seleccione un producto.');
                return;
            }

            const idArticulo = selectedOption.value;
            const nombre = selectedOption.getAttribute('data-nombre');
            const precio = parseFloat(document.getElementById('modalprecio').value);
            const cantidad = parseInt(document.getElementById('modalcantidad').value);
            const stock = parseInt(selectedOption.getAttribute('data-stock'));

            if (isNaN(cantidad) || cantidad < 1) {
                alert('Por favor, ingrese una cantidad válida.');
                return;
            }

            if (cantidad > stock) {
                alert('La cantidad solicitada supera el stock disponible. Stock disponible: ' + stock);
                return;
            }

            const subTotal = precio * cantidad;
            const igv = subTotal * IGV_RATE;

            console.log("Agregando producto:", {idArticulo, nombre, precio, cantidad, subTotal, igv});

            // Crear nueva fila
            const newRow = tablaItems.insertRow();
            newRow.className = 'item-row';

            // Agregar celdas
            const cellAccion = newRow.insertCell(0);
            const cellItem = newRow.insertCell(1);
            const cellDesc = newRow.insertCell(2);
            const cellPrecio = newRow.insertCell(3);
            const cellCantidad = newRow.insertCell(4);
            const cellIgv = newRow.insertCell(5);
            const cellSubtotal = newRow.insertCell(6);

            // Llenar celdas
            cellAccion.innerHTML = `<a href="#" class="btn btn-danger btn-sm btn-eliminar">Eliminar</a>`;
            cellItem.innerText = itemCounter++;
            cellDesc.innerText = nombre;
            cellPrecio.innerText = precio.toFixed(2);
            cellCantidad.innerText = cantidad;
            cellIgv.innerText = igv.toFixed(2);
            cellSubtotal.innerText = subTotal.toFixed(2);

            // Agregar campos hidden usando DOM
            const inputId = document.createElement('input');
            inputId.type = 'hidden';
            inputId.name = 'idArticulo';
            inputId.value = idArticulo;
            newRow.appendChild(inputId);

            const inputCantidad = document.createElement('input');
            inputCantidad.type = 'hidden';
            inputCantidad.name = 'cantidad';
            inputCantidad.value = cantidad;
            newRow.appendChild(inputCantidad);

            const inputPrecio = document.createElement('input');
            inputPrecio.type = 'hidden';
            inputPrecio.name = 'precio';
            inputPrecio.value = precio.toFixed(2);
            newRow.appendChild(inputPrecio);

            console.log("✅ Producto agregado a la tabla. Hidden fields creados.");

            // Limpiar y cerrar modal
            selectProducto.selectedIndex = 0;
            document.getElementById("modalnombre").value = "";
            document.getElementById("modalstock").value = "";
            document.getElementById("modalprecio").value = "";
            document.getElementById("modalcantidad").value = "1";

            modal.style.display = 'none';

            actualizarTotales();
        });

        tablaItems.addEventListener('click', function (e) {
            if (e.target.classList.contains('btn-eliminar')) {
                e.preventDefault();
                const row = e.target.closest('tr');
                row.remove();
                actualizarTotales();
            }
        });

    });
</script>

<script>
    // Validar que haya items antes de enviar el formulario
    document.querySelector("form").addEventListener("submit", function (e) {
        console.log("=== VALIDANDO FORMULARIO ===");

        // Asegurar que los totales estén actualizados
        actualizarTotales();

        // Validar que haya al menos un item en el pedido
        const tablaItems = document.querySelector('#tablaItems tbody');
        const rows = tablaItems ? tablaItems.rows : [];
        console.log("Número de filas en la tabla:", rows.length);

        if (rows.length === 0) {
            e.preventDefault();
            alert('El pedido debe tener al menos un producto.');
            return;
        }

        // Validar que se haya seleccionado un cliente
        const idCliente = document.getElementById('idCliente').value;
        console.log("Cliente seleccionado:", idCliente);

        if (!idCliente) {
            e.preventDefault();
            alert('Debe seleccionar un cliente.');
            return;
        }

        console.log('✅ Formulario válido - Enviando con ' + rows.length + ' items');
    });
</script>