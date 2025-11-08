# The Last Stand - JavaFX Setup Guide

## Yêu cầu hệ thống / System Requirements

- **Java JDK 17 hoặc cao hơn / Java JDK 17 or higher**
  - Tải về / Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) hoặc [OpenJDK](https://adoptium.net/)
  - Đảm bảo `JAVA_HOME` được cài đặt đúng / Ensure `JAVA_HOME` is set correctly

## Cách chạy dự án / How to Run the Project

### Windows

```powershell
# Bước 1: Clone repository
git clone <repository-url>
cd The-Last-Stand

# Bước 2: Chạy game với Maven Wrapper
.\mvnw.cmd clean javafx:run
```

### macOS / Linux

```bash
# Bước 1: Clone repository
git clone <repository-url>
cd The-Last-Stand

# Bước 2: Cấp quyền thực thi cho Maven Wrapper
chmod +x mvnw

# Bước 3: Chạy game
./mvnw clean javafx:run
```

## Giải thích / Explanation

- **Không cần cài đặt Maven**: Maven Wrapper (mvnw/mvnw.cmd) sẽ tự động tải Maven về nếu chưa có.
- **Không cần cài đặt JavaFX**: Các thư viện JavaFX sẽ tự động tải qua Maven dependencies.
- **Chỉ cần Java JDK 17+**: Đây là yêu cầu duy nhất cần cài đặt thủ công.

- **No Maven installation needed**: Maven Wrapper will automatically download Maven if not present.
- **No JavaFX installation needed**: JavaFX libraries will be downloaded automatically via Maven dependencies.
- **Only Java JDK 17+ required**: This is the only manual installation needed.

## Build dự án / Build the Project

```bash
# Windows
.\mvnw.cmd clean compile

# macOS/Linux
./mvnw clean compile
```

## Xử lý sự cố / Troubleshooting

### Lỗi "JAVA_HOME not found"
- **Windows**: Cài đặt biến môi trường
  ```powershell
  # Tạm thời (chỉ cho phiên hiện tại)
  $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
  
  # Vĩnh viễn: System Properties → Environment Variables → New
  # Variable name: JAVA_HOME
  # Variable value: C:\Program Files\Java\jdk-17
  ```

- **macOS/Linux**: Thêm vào `~/.bashrc` hoặc `~/.zshrc`
  ```bash
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
  export PATH=$JAVA_HOME/bin:$PATH
  ```

### Lỗi kết nối tải Maven Wrapper
Nếu không thể tải maven-wrapper.jar tự động, tải thủ công:
1. Tải về: https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
2. Đặt vào thư mục: `.mvn/wrapper/maven-wrapper.jar`

If maven-wrapper.jar cannot be downloaded automatically, download manually:
1. Download: https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
2. Place in: `.mvn/wrapper/maven-wrapper.jar`

## Cấu trúc dự án / Project Structure

```
The-Last-Stand/
├── Constant/          # Hằng số game / Game constants
├── Entities/          # Các đối tượng trong game / Game entities
├── Managers/          # Quản lý logic / Logic managers
├── Scene/             # Các màn hình / Game scenes
├── UI/                # Giao diện người dùng / UI components
├── resource/          # Tài nguyên (sprites, images) / Resources
├── pom.xml            # Maven configuration
├── mvnw / mvnw.cmd    # Maven Wrapper scripts
└── .mvn/              # Maven Wrapper configuration
```

## Công nghệ sử dụng / Technologies Used

- **Java 17**
- **JavaFX 21.0.4** (UI Framework)
- **Maven 3.9.6** (Build Tool)
- **Maven Wrapper** (Portable build execution)

## Phát triển / Development

Sử dụng IDE như IntelliJ IDEA, Eclipse, hoặc VS Code với Java Extension Pack.

Use IDEs like IntelliJ IDEA, Eclipse, or VS Code with Java Extension Pack.

### Import vào IntelliJ IDEA
1. File → Open → Chọn thư mục `The-Last-Stand`
2. IntelliJ sẽ tự động nhận diện Maven project
3. Chờ Maven tải dependencies
4. Run: Chạy Main class `Main.Game`

### Import vào VS Code
1. Mở thư mục `The-Last-Stand`
2. Extension Pack for Java sẽ tự động cấu hình
3. Chờ Maven tải dependencies
4. Run: F5 hoặc Run → Start Debugging
