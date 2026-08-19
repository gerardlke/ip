param(
    [string]$PlanPath = "test/ui-test-plan.md"
)

$ErrorActionPreference = "Stop"

function Normalize-Output([string]$text) {
    return $text.Replace("`r`n", "`n").TrimEnd("`r", "`n")
}

if (-not (Test-Path -LiteralPath $PlanPath)) {
    throw "UI test plan not found: $PlanPath"
}

$sourceFiles = Get-ChildItem -Path "src/main/java" -Filter "*.java" -File
if ($sourceFiles.Count -eq 0) {
    throw "No Java source files found in src/main/java."
}

$classesDirectory = Join-Path $env:TEMP "finn-ui-test-$PID"
New-Item -ItemType Directory -Path $classesDirectory | Out-Null

try {
    & javac --release 25 -d $classesDirectory $sourceFiles.FullName
    if ($LASTEXITCODE -ne 0) {
        throw "Compilation failed. UI tests were not run."
    }

    $plan = Get-Content -Raw -LiteralPath $PlanPath
    $testPattern = '(?ms)^## Test: (?<name>.+?)\r?$\s*Aim:\s*(?<aim>.+?)\r?\n\s*Inputs:\s*```text\r?\n(?<inputs>.*?)\r?\n```\s*Expected output:\s*```text\r?\n(?<expected>.*?)\r?\n```'
    $tests = [regex]::Matches($plan, $testPattern)

    if ($tests.Count -eq 0) {
        throw "No tests matched the required format in $PlanPath."
    }

    foreach ($test in $tests) {
        $name = $test.Groups['name'].Value.Trim()
        $inputs = $test.Groups['inputs'].Value
        $expected = Normalize-Output $test.Groups['expected'].Value
        $actual = Normalize-Output (($inputs | & java -cp $classesDirectory Finn 2>&1 | Out-String))

        Write-Host "`n=== $name ==="
        Write-Host "Aim: $($test.Groups['aim'].Value.Trim())"
        Write-Host "Console input:"
        Write-Host $inputs
        Write-Host "Console output:"
        Write-Host $actual

        if ($actual -cne $expected) {
            Write-Host "FAILED: $name"
            Write-Host "Expected output:"
            Write-Host $expected
            Write-Host "Actual output:"
            Write-Host $actual
            exit 1
        }

        Write-Host "PASSED: $name"
    }

    Write-Host "`nAll $($tests.Count) UI test case(s) passed."
} finally {
    if (Test-Path -LiteralPath $classesDirectory) {
        Remove-Item -LiteralPath $classesDirectory -Recurse -Force
    }
}
