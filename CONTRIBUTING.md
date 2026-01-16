# Flujo de Git - GitFlow

Este proyecto sigue el modelo **GitFlow** para gestión de ramas.

---

## Estructura de ramas

| Rama | Propósito | Merge desde | Merge hacia |
|------|-----------|-------------|-------------|
| `main` | Producción (releases estables) | `release/*`, `hotfix/*` | - |
| `develop` | Integración de features | `feature/*` | `main` (via release) |
| `feature/*` | Nuevas funcionalidades | - | `develop` |
| `release/*` | Preparación de release | `develop` | `main`, `develop` |
| `hotfix/*` | Correcciones urgentes en producción | `main` | `main`, `develop` |

---

## Flujo de trabajo

### 1. Nueva funcionalidad

```bash
git checkout develop
git checkout -b feature/nombre-funcionalidad
# ... desarrollo y commits ...
git push -u origin feature/nombre-funcionalidad
```

### 2. Finalizar feature

```bash
git checkout develop
git merge feature/nombre-funcionalidad --no-ff
git branch -d feature/nombre-funcionalidad
git push origin develop
```

### 3. Crear release

```bash
git checkout develop
git checkout -b release/v1.0.0
# ... ajustes finales, versionado ...
git checkout main
git merge release/v1.0.0 --no-ff
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin main --tags
git push origin develop
```

### 4. Hotfix urgente

```bash
git checkout main
git checkout -b hotfix/nombre-fix
# ... corrección ...
git checkout main
git merge hotfix/nombre-fix --no-ff
git tag -a v1.0.1 -m "Hotfix v1.0.1"
git push origin main --tags
git checkout develop
git merge hotfix/nombre-fix --no-ff
git push origin develop
```

---

## Reglas de protección

- **main**: Requiere PR + 1 aprobación
- **develop**: Requiere PR + 1 aprobación
- No se permite push directo a `main` ni `develop`
