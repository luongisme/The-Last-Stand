# 🚀 Hướng dẫn Setup cho Team Members

## 📋 Checklist trước khi bắt đầu

- [ ] Đã cài Java JDK 17 hoặc cao hơn
- [ ] Đã cài Git
- [ ] Đã cài VS Code + Extension Pack for Java (nếu dùng VS Code)

## 🔧 Setup từ đầu (Lần đầu tiên)

### Bước 1: Clone Repository

```powershell
git clone https://github.com/luongisme/The-Last-Stand.git
cd The-Last-Stand
```

### Bước 2: Kiểm tra Java

```powershell
# Kiểm tra version
java -version

# Phải hiển thị: java version "17.x.x" hoặc cao hơn
# VÍ DỤ:
# java version "17.0.12" 
# hoặc
# java version "21.0.x"

# Kiểm tra JAVA_HOME
$env:JAVA_HOME

# Phải trỏ đến thư mục JDK, VÍ DỤ:
# C:\Program Files\Java\jdk-17
# C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot
```

**⚠️ Nếu java -version hiển thị version khác với JAVA_HOME:**

```powershell
# Tìm đường dẫn java command
Get-Command java | Select-Object Source

# Nếu Source không trùng với JAVA_HOME, cần fix PATH
# Ví dụ: Source = C:\Program Files\Common Files\Oracle\Java\javapath\java.exe
# Nhưng JAVA_HOME = C:\Program Files\Java\jdk-17

# FIX: Thêm JAVA_HOME vào đầu PATH
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# Kiểm tra lại
java -version
```

### Bước 3: Tải Dependencies (QUAN TRỌNG!)

```powershell
# Chạy lệnh này - sẽ mất 2-5 phút lần đầu
.\mvnw.cmd clean install -U

# Chờ đến khi thấy: [INFO] BUILD SUCCESS
```

**Lệnh này làm gì?**
- Tải Maven về (nếu chưa có)
- Tải JavaFX libraries
- Tải tất cả dependencies
- Compile project
- Lưu vào `~/.m2/repository` (cache local)

### Bước 4: Test chạy game

```powershell
.\mvnw.cmd javafx:run

# Nếu game hiện lên → SUCCESS! ✅
# Nếu lỗi → Xem phần "Troubleshooting" bên dưới
```

---

## 🖥️ Setup VS Code

### Bước 1: Cài Extensions

Mở VS Code → Extensions (Ctrl+Shift+X) → Tìm và cài:

1. **Extension Pack for Java** (Microsoft) - BẮT BUỘC
2. **Maven for Java** (Microsoft) - Tự động cài kèm Extension Pack

### Bước 2: Tạo settings.json

```powershell
# Trong thư mục project
Copy-Item .vscode\settings.json.example .vscode\settings.json
```

Hoặc tạo thủ công file `.vscode/settings.json`:

```json
{
    "java.debug.settings.onBuildFailureProceed": true,
    "java.configuration.updateBuildConfiguration": "automatic",
    "java.import.maven.enabled": true,
    "java.compile.nullAnalysis.mode": "automatic",
    "maven.executable.path": "${workspaceFolder}/mvnw.cmd"
}
```

### Bước 3: Reload VS Code

```
Ctrl+Shift+P → Gõ: "Developer: Reload Window"
```

### Bước 4: Đợi Java Extension load

Nhìn góc phải dưới VS Code:
- Sẽ thấy: "Importing project..." hoặc "Building workspace..."
- Đợi đến khi xong (1-3 phút)
- Không có lỗi đỏ ở thanh Problems (Ctrl+Shift+M)

### Bước 5: Nếu vẫn có lỗi

```
1. Ctrl+Shift+P
2. Gõ: "Java: Clean Java Language Server Workspace"
3. Chọn "Restart and delete"
4. Chờ VS Code restart
5. Ctrl+Shift+P → "Developer: Reload Window"
```

---

## 🐛 Troubleshooting - Các lỗi thường gặp

### ❌ Lỗi: "The project was not built since its build path is incomplete"

**Nguyên nhân**: VS Code Java Extension chưa build Maven project

**Fix**:
```powershell
# Bước 1
.\mvnw.cmd clean install -U

# Bước 2: Clean Java workspace
# Ctrl+Shift+P → "Java: Clean Java Language Server Workspace"
# → "Restart and delete"

# Bước 3
# Ctrl+Shift+P → "Developer: Reload Window"
```

---

### ❌ Lỗi: "The package Main does not exist" (179 errors)

**Nguyên nhân**: Java Language Server chưa nhận diện module structure

**Fix 1** - Clean và rebuild:
```
1. Ctrl+Shift+P → "Java: Clean Java Language Server Workspace"
2. Chọn "Restart and delete"
3. Đợi reload xong
4. Ctrl+Shift+P → "Java: Force Java compilation"
```

**Fix 2** - Xóa cache hoàn toàn:
```powershell
# Đóng VS Code
Remove-Item -Recurse -Force "$env:APPDATA\Code\User\workspaceStorage"
# Mở lại VS Code
```

---

### ❌ Lỗi: "java.lang.Object cannot be resolved"

**Nguyên nhân**: `java` command trong PATH trỏ sai JDK

**Kiểm tra**:
```powershell
java -version           # Ví dụ: java version "17.0.12"
$env:JAVA_HOME          # Ví dụ: C:\Program Files\Java\jdk-17
Get-Command java | Select-Object Source
```

**Nếu `java -version` khác với `JAVA_HOME`:**

```powershell
# Tạm thời (chỉ session hiện tại)
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# Hoặc fix vĩnh viễn:
# 1. Windows Search → "Environment Variables"
# 2. System Properties → Environment Variables
# 3. Trong "System variables" → Tìm "Path" → Edit
# 4. Thêm vào ĐẦU: C:\Program Files\Java\jdk-17\bin
# 5. Xóa các path Java cũ (java8path, javapath cũ)
# 6. Restart VS Code
```

---

### ❌ Lỗi: "Error occurred during initialization of boot layer"

**Nguyên nhân**: File `.class` hoặc thư mục lạ trong source folders

**Fix**:
```powershell
.\mvnw.cmd clean
Remove-Item -Recurse -Force target
```

---

### ❌ Maven Wrapper không tải được

**Fix**:
```powershell
# Download manual
Invoke-WebRequest -Uri "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" -OutFile ".mvn/wrapper/maven-wrapper.jar"

# Thử lại
.\mvnw.cmd clean compile
```

---

## ✅ Checklist khi pull code mới

```powershell
# 1. Pull code
git pull

# 2. Update dependencies (nếu pom.xml thay đổi)
.\mvnw.cmd clean install -U

# 3. Nếu VS Code báo lỗi
# Ctrl+Shift+P → "Java: Clean Java Language Server Workspace"

# 4. Run
.\mvnw.cmd javafx:run
```

---

## 📞 Vẫn gặp vấn đề?

1. Chụp screenshot lỗi
2. Chạy và gửi kết quả:
```powershell
java -version
$env:JAVA_HOME
Get-Command java | Select-Object Source
.\mvnw.cmd -version
```
3. Tag team lead trong group chat

---

## 🎯 TL;DR - Tóm tắt nhanh

```powershell
# Clone
git clone https://github.com/luongisme/The-Last-Stand.git
cd The-Last-Stand

# Setup (lần đầu)
.\mvnw.cmd clean install -U

# Copy VS Code config
Copy-Item .vscode\settings.json.example .vscode\settings.json

# Run
.\mvnw.cmd javafx:run

# Nếu lỗi trong VS Code
# Ctrl+Shift+P → "Java: Clean Java Language Server Workspace"
```

✨ **Chúc may mắn!**
