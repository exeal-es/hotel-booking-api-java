# Corporate Hotel Booking API

Crear un motor corporativos de reservas de hoteles. Este motor debe satisfacer las necesidades de tres tipos diferentes de actores.

### Gerente del Hotel

*Rol: Establecer todos los diferentes tipos de habitaciones y cantidades respectivas para su hotelModel*

Casos de uso:

- Recuperar toda la información sobre el número de habitaciones de un hotelModel dado.
- Actualizar la cantidad de cierto tipo de habitación para un hotelModel dado.
    - Un cambio en la cantidad de habitaciones no debe afectar las reservas existentes. Solo afectarán las nuevas reservas realizadas después del cambio.

### Administrador de la Empresa

*Rol: Agregar/eliminar empleados y también crear políticas de reserva para su empresa y empleados.*

Casos de uso:

- Agregar empleado
- Eliminar un empleado
- Agregar Política de Reserva de la Empresa: Indica qué tipo de habitaciones se pueden reservar.
    - Ej. una empresa puede permitir solo la reserva de habitaciones estándar (individual/doble). O puede permitir la reserva de habitaciones estándar y suites junior.
- Agregar Política de Reserva del Empleado: Indica qué tipo de habitaciones puede reservar un empleado específico.
    - Ej. Un empleado puede tener permitido reservar solo una habitación estándar, mientras que otro empleado puede tener permitido reservar habitaciones estándar, suites junior y suites master.

Reglas:

- Los empleados no deben duplicarse.
- Al eliminar un empleado, todas las reservas y políticas asociadas al empleado también deben ser eliminadas del sistema.
- Las políticas de los empleados tienen prioridad sobre las políticas de la empresa. Si existe una política para un empleado, esta debe ser respetada independientemente de lo que diga la política de la empresa (si existe).
- Si no existe una política para un empleado, se debe verificar la política de la empresa.
- Si no existen políticas ni para el empleado ni para la empresa, se debe permitir al empleado reservar cualquier habitación.

### Empleado

*Rol: Reservar una habitación de hotelModel*

Casos de uso:

- Permitir a los empleados reservar habitaciones en hoteles.

Reglas:

- La fecha de salida debe ser al menos un día después de la fecha de entrada.
- Validar si el hotelModel existe y si el tipo de habitación es proporcionado por el hotelModel.
- Verificar si la reserva está permitida según las políticas de reserva definidas, si las hay. Ver el Servicio de Políticas de Reserva para más detalles.
- La reserva solo debe permitirse si hay al menos un tipo de habitación disponible durante todo el período de la reserva.
- Mantener un registro de todas las reservas. Ej. Si el hotelModel tiene 5 habitaciones estándar, no debe haber más de 5 reservas para el mismo día.
- Las habitaciones de hotelModel se pueden reservar muchas veces siempre que no haya conflictos con las fechas.
- Devolver la confirmación de la reserva al empleado o un error en caso contrario (también se pueden usar excepciones).