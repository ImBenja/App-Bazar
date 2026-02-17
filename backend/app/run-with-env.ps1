$envFile = Join-Path $PSScriptRoot ".env"

if (-not (Test-Path $envFile)) {
  Write-Error "No se encontro $envFile"
  exit 1
}

Get-Content $envFile | ForEach-Object {
  $line = $_.Trim()

  if (-not $line -or $line.StartsWith("#")) {
    return
  }

  $parts = $line -split "=", 2
  if ($parts.Length -eq 2) {
    [System.Environment]::SetEnvironmentVariable($parts[0], $parts[1], "Process")
  }
}

& "$PSScriptRoot\\mvnw.cmd" spring-boot:run
