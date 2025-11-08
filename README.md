## Versiones de tecnologías usadas

- Java 21.0.6
- Gradle 9.2.0
- Serenity BDD 3.9.8
- JUnit 5.9.2
- Navegador Chrome (última versión)

## Instrucciones de ejecución

1. Clonar este repositorio 
```bash
git clone https://github.com/kjduy/automatizacion-e2e.git
cd automatizacion-e2e
```

2. Instalar las dependencias
```bash
./gradlew clean build
```

3. Acceder a la página https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
4. Ingresar con el usuario Admin y contraseña admin123
5. Verificar que exista el usuario Jasmine.Morgan, si no existe, se debe crear

6. Ejecutar las pruebas
```bash
./gradlew clean test aggregate
```

El reporte de pruebas se generará en: `target/site/serenity/index.html`
