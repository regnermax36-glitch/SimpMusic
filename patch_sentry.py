import sys
with open("androidApp/build.gradle.kts", "r") as f:
    text = f.read()

# Try to make Sentry gracefully skip release by omitting upload requirements
text = text.replace("autoUploadProguardMapping.set(true)", "autoUploadProguardMapping.set(false)")
text = text.replace("includeProguardMapping.set(true)", "includeProguardMapping.set(false)")

with open("androidApp/build.gradle.kts", "w") as f:
    f.write(text)
