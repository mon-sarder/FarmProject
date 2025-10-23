# 🚀 **EC2 Deployment Guide: Spring Boot + React on ARM64 (Graviton)**

This guide walks you through deploying your full‑stack application to an **AWS EC2** instance from an **M2 Mac**. We’ll use an **ARM64 (Graviton)** instance, which is compatible with Apple Silicon and offers great price/performance.

> **Apps covered:**
> - **Backend:** Java + Spring Boot
> - **Frontend:** React + TypeScript (Vite) served via **Nginx**

---

## 🧰 **Part 1 — Build Your Apps (On Your Mac)**

First, build the **production** versions of your apps locally.

### 1) Build the **Backend** (Java)

In your Mac terminal, navigate to your backend folder and build the executable `.jar` file:
```bash
# Navigate to the backend root
cd FarmProject/web-app/backend

# Run the Maven package command
mvn clean package -DskipTests

# This creates your app at: target/queueinv-0.0.1-SNAPSHOT.jar
# Rename it for simplicity:
cp target/queueinv-0.0.1-SNAPSHOT.jar target/backend.jar
```

### 2) Build the **Frontend** (React)

In another terminal, navigate to your frontend folder and build the static files:
```bash
# Navigate to the frontend root
cd FarmProject/web-app/frontend

# Install dependencies (if you haven't)
npm install

# Build the static files
npm run build

# This creates a 'dist' folder inside 'web-app/frontend/'.
# This 'dist' folder contains your index.html, JS, and CSS.
```

---

## 🖥️ **Part 2 — Launch EC2 Instance (AWS Console)**

1. Go to the **EC2 Dashboard** and click **Launch instances**.
2. **Name:** `farm-app-server` (or anything you like)
3. **Application and OS Images:** Select **Ubuntu**.
4. **Instance type:**
    - Click **Instance type** and search for **t4g.nano** or **t4g.micro**.
    - Select one. The **t4g** series are **ARM64 (Graviton)** processors ✅
5. **Key pair (login):**
    - Click **Create new key pair**
    - **Name:** `farm-app-key`
    - **Key pair type:** `RSA`
    - **Private key file format:** `.pem`
    - Click **Create key pair**. It will download `farm-app-key.pem` to your Mac.
    - **Important:** move this file to a safe place, e.g. `~/.ssh/`
6. **Network settings (Firewall):** Click **Edit** and ensure **Create security group** is selected. Add inbound rules:
    - **Rule 1:** Type **SSH**, Source **My IP** (for security)
    - **Rule 2:** Type **HTTP**, Source **Anywhere (0.0.0.0/0)**
    - **Rule 3:** Type **HTTPS**, Source **Anywhere (0.0.0.0/0)**
7. **Launch Instance** and wait for **Running** state.
8. **Get your public IP** from the instance details (Public IPv4 address).

---

## 🔐 **Part 3 — Connect & Install Server Software (Mac → Server)**

### Set Key Permissions (Only once)
```bash
chmod 400 ~/.ssh/farm-app-key.pem
```

### Connect via SSH
> Replace `YOUR_PUBLIC_IP` with the IP you copied from AWS.
```bash
ssh -i "~/.ssh/farm-app-key.pem" ubuntu@YOUR_PUBLIC_IP
```
Type `yes` when prompted. You are now inside your server.

### Install Software (On the Server)
Run these commands **inside the SSH session** to install Java (for backend) and Nginx (for frontend).
```bash
# Update the server's package lists
sudo apt update

# Install Java 17 (for Spring Boot)
sudo apt install -y openjdk-17-jdk

# Install Nginx (to serve React)
sudo apt install -y nginx
```

---

## ⬆️ **Part 4 — Upload Your Files (New Mac Terminal)**

Open a **new Mac terminal window** (keep the SSH one open). Use `scp` to upload your built files from **Part 1**.

### Upload Backend `.jar`
```bash
# Make sure to replace YOUR_PUBLIC_IP
scp -i "~/.ssh/farm-app-key.pem" ~/path/to/FarmProject/web-app/backend/target/backend.jar ubuntu@YOUR_PUBLIC_IP:/home/ubuntu/
```

### Upload Frontend `dist` folder
```bash
# The -r flag is for "recursive" (copy the whole folder)
scp -i "~/.ssh/farm-app-key.pem" -r ~/path/to/FarmProject/web-app/frontend/dist ubuntu@YOUR_PUBLIC_IP:/home/ubuntu/
```

---

## ⚙️ **Part 5 — Configure & Run (Back in SSH Terminal)**

Go back to your **SSH terminal** (the one connected to the server).

### Move Frontend Files
Move the built React files into the directory Nginx serves.
```bash
# Create the app directory
sudo mkdir -p /var/www/farm-app-ui

# Move the contents of 'dist' into the new directory
sudo mv ~/dist/* /var/www/farm-app-ui/
```

### Configure Nginx
We need to tell Nginx to serve your React app and, if needed, **proxy API calls** to your Spring Boot app.

Open the default site config with nano:
```bash
sudo nano /etc/nginx/sites-available/default
```

**Delete everything** in the file and paste this configuration:

```nginx
server {
    listen 80 default_server;
    listen [::]:80 default_server;

    server_name _;

    root /var/www/farm-app-ui;
    index index.html;

    # Serve static files and let React handle client-side routing
    location / {
        try_files $uri /index.html;
    }

    # (Optional) Proxy API to Spring Boot if you use a path like /api
    # Adjust target port if your backend runs on a different port.
    location /api/ {
        proxy_pass http://127.0.0.1:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

Save and exit (`Ctrl+X`, then `Y`, then `Enter`).

Test and restart Nginx:
```bash
# Test your config file for syntax errors
sudo nginx -t

# If it says "syntax is ok", restart Nginx to apply changes
sudo systemctl restart nginx
```

### Run the Backend App
Run your Java backend with `nohup` so it keeps running after you disconnect.
```bash
# Run the backend.jar file in the background
nohup java -jar ~/backend.jar > backend.log 2>&1 &

# The '>' part saves all console output to 'backend.log' for debugging
```

---

## 🎉 **You're Live!**
Your application should now be accessible to the world. Open:
```
http://YOUR_PUBLIC_IP
```
and you should see your React app. API requests to `/api/...` will be proxied to your Spring Boot server on the same host.

> ✅ **Tip:** For a custom domain + HTTPS, add a DNS A-record pointing to your IP and use **Certbot** (Let’s Encrypt) with Nginx.

---

## 🧩 **Troubleshooting Quick Checks**
- `sudo systemctl status nginx` — Ensure Nginx is running.
- `tail -f /var/log/nginx/error.log` — Watch Nginx errors live.
- `tail -f ~/backend.log` — Watch your Spring Boot logs.
- Security Group allows **HTTP (80)**, **HTTPS (443)**, and **SSH (22)**.
- `curl -I http://127.0.0.1` on the server should return HTTP headers from Nginx.

---

## 🧹 **Cleanup & Maintenance**
- To stop the backend: `pkill -f backend.jar` or find with `ps aux | grep backend.jar`.
- Consider creating a **systemd service** for the backend for auto‑restart on reboot.
- Regularly update the server: `sudo apt update && sudo apt upgrade -y`.
