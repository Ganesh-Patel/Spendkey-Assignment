# Deployment Guide

## Backend Deployment (Spring Boot)

### Option 1: Railway (Recommended)
1. Go to [Railway](https://railway.app/)
2. Create an account and connect your GitHub repository
3. Create a new project and select your repository
4. Railway will automatically detect it's a Spring Boot application
5. Add environment variables:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `SPRING_DATASOURCE_URL=your-mysql-connection-string`
   - `SPRING_DATASOURCE_USERNAME=your-username`
   - `SPRING_DATASOURCE_PASSWORD=your-password`
6. Deploy and get your backend URL (e.g., `https://your-app.railway.app`)

### Option 2: Heroku
1. Create a Heroku account
2. Install Heroku CLI
3. Create a new Heroku app
4. Add MySQL addon (ClearDB or JawsDB)
5. Deploy using Git:
   ```bash
   heroku create your-app-name
   git push heroku main
   ```

### Option 3: DigitalOcean App Platform
1. Go to DigitalOcean App Platform
2. Connect your GitHub repository
3. Select the backend directory
4. Configure environment variables
5. Deploy

## Frontend Deployment (Angular)

### Vercel Deployment
1. Go to [Vercel](https://vercel.com/)
2. Create an account and connect your GitHub repository
3. Import your repository
4. Configure the project:
   - Framework Preset: Angular
   - Root Directory: `ecommerce-frontend`
   - Build Command: `npm run build`
   - Output Directory: `dist/ecommerce-frontend`
5. Add Environment Variables:
   - `API_URL`: Your backend URL (e.g., `https://your-app.railway.app/api`)
6. Deploy

### Update Environment Configuration
After deploying your backend, update the production environment file:

```typescript
// src/environments/environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://your-actual-backend-url.com/api' // Replace with your backend URL
};
```

### Alternative: Netlify
1. Go to [Netlify](https://netlify.com/)
2. Connect your GitHub repository
3. Configure build settings:
   - Build command: `npm run build`
   - Publish directory: `dist/ecommerce-frontend`
4. Add environment variables
5. Deploy

## Environment Variables

### Backend Environment Variables
```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:mysql://your-host:3306/your-database
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
JWT_SECRET=your-jwt-secret-key
```

### Frontend Environment Variables
```bash
API_URL=https://your-backend-url.com/api
```

## Database Setup

### Option 1: Railway MySQL
1. Add MySQL service to your Railway project
2. Get connection details from Railway dashboard
3. Update backend environment variables

### Option 2: PlanetScale
1. Create account at [PlanetScale](https://planetscale.com/)
2. Create a new database
3. Get connection string and update backend

### Option 3: AWS RDS
1. Create MySQL RDS instance
2. Configure security groups
3. Update backend connection string

## CORS Configuration

Make sure your backend CORS configuration allows your frontend domain:

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "https://your-frontend-domain.vercel.app",
            "http://localhost:4200"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

## Troubleshooting

### Common Issues:
1. **CORS Errors**: Update backend CORS configuration
2. **Database Connection**: Check connection string and credentials
3. **Environment Variables**: Ensure all variables are set correctly
4. **Build Errors**: Check for missing dependencies

### Build Commands:
```bash
# Backend
./mvnw clean package -DskipTests

# Frontend
npm install
npm run build
```

## Monitoring

After deployment:
1. Test all API endpoints
2. Verify frontend-backend communication
3. Check database connections
4. Monitor application logs
5. Test user registration and login
6. Verify cart and order functionality 