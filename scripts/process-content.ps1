# FANDEX Content Processor v2
# Scans content/{category}/{module}/ structure, generates index.json,
# copies .md files to Android assets directory

$ModuleCategoryMap = @{
    "agent"              = "ai"
    "ai-ethics"          = "ai"
    "ai-engineering"     = "ai"
    "llm"                = "ai"
    "deep-learning"      = "ai"
    "machine-learning"   = "ai"
    "nlp"                = "ai"
    "computer-vision"    = "ai"
    "multimodal"         = "ai"
    "generative-ai"      = "ai"
    "python"             = "ai"
    "html5"              = "frontend"
    "css"                = "frontend"
    "javascript"         = "frontend"
    "typescript"         = "frontend"
    "react"              = "frontend"
    "vue3"               = "frontend"
    "java"               = "backend"
    "go"                 = "backend"
    "csharp"             = "backend"
    "kotlin"             = "backend"
    "lua"                = "backend"
    "mysql"              = "database"
    "postgresql"         = "database"
    "redis"              = "database"
    "sql"                = "database"
    "big-data"           = "database"
    "c"                  = "cs"
    "cpp"                = "cs"
    "algorithm"          = "cs"
    "cs-fundamentals"    = "cs"
    "discrete-math"      = "cs"
    "networking"         = "cs"
    "cybersecurity"      = "cs"
    "software-architecture"  = "cs"
    "software-engineering"   = "cs"
    "software-testing"       = "cs"
    "engineering-practices"  = "cs"
    "calculus"               = "math"
    "probability-statistics" = "math"
    "data-analysis"          = "math"
    "linear-algebra"         = "math"
    "cloud-computing"    = "cloud"
    "iot"                = "cloud"
    "harmonyos"          = "cloud"
    "devops"             = "tools"
    "git"                = "tools"
    "github"             = "tools"
    "markdown"           = "tools"
    "getting-started"    = "tools"
    "english"            = "tools"
}

$Categories = @(
    @{ id = "tools";    label = [char]0x5DE5 + [char]0x5177 + [char]0x94FE;       color = "#4f5bd5" }
    @{ id = "frontend"; label = [char]0x524D + [char]0x7AEF + [char]0x6280 + [char]0x672F;     color = "#d63031" }
    @{ id = "backend";  label = [char]0x540E + [char]0x7AEF + [char]0x6280 + [char]0x672F;     color = "#e17055" }
    @{ id = "database"; label = [char]0x6570 + [char]0x636E + [char]0x5E93;       color = "#00b894" }
    @{ id = "cs";       label = [char]0x8BA1 + [char]0x7B97 + [char]0x673A + [char]0x79D1 + [char]0x5B66;   color = "#8854d0" }
    @{ id = "math";     label = [char]0x6570 + [char]0x5B66;         color = "#6c5ce7" }
    @{ id = "cloud";    label = [char]0x4E91 + [char]0x4E0E + [char]0x57FA + [char]0x7840 + [char]0x8BBE + [char]0x65BD; color = "#e05a2b" }
    @{ id = "ai";       label = [char]0x4EBA + [char]0x5DE5 + [char]0x667A + [char]0x80FD;     color = "#f9a825" }
)

$ModuleLabels = @{
    "agent"              = "AI Agent"
    "ai-ethics"          = "AI Ethics & Safety"
    "ai-engineering"     = "AI Engineering"
    "llm"                = "LLM"
    "deep-learning"      = "Deep Learning"
    "machine-learning"   = "Machine Learning"
    "nlp"                = "NLP"
    "computer-vision"    = "Computer Vision"
    "multimodal"         = "Multimodal"
    "generative-ai"      = "Generative AI"
    "python"             = "Python"
    "html5"              = "HTML5"
    "css"                = "CSS"
    "javascript"         = "JavaScript"
    "typescript"         = "TypeScript"
    "react"              = "React"
    "vue3"               = "Vue 3"
    "java"               = "Java"
    "go"                 = "Go"
    "csharp"             = "C#"
    "kotlin"             = "Kotlin"
    "lua"                = "Lua"
    "mysql"              = "MySQL"
    "postgresql"         = "PostgreSQL"
    "redis"              = "Redis"
    "sql"                = "SQL"
    "big-data"           = "Big Data"
    "c"                  = "C"
    "cpp"                = "C++"
    "algorithm"          = "Algorithm"
    "cs-fundamentals"    = "CS Fundamentals"
    "discrete-math"      = "Discrete Math"
    "networking"         = "Networking"
    "cybersecurity"      = "Cybersecurity"
    "software-architecture" = "Software Architecture"
    "software-engineering"  = "Software Engineering"
    "software-testing"      = "Software Testing"
    "engineering-practices" = "Engineering Practices"
    "calculus"           = "Calculus"
    "probability-statistics" = "Probability & Statistics"
    "data-analysis"      = "Data Analysis"
    "linear-algebra"     = "Linear Algebra"
    "cloud-computing"    = "Cloud Computing"
    "iot"                = "IoT"
    "harmonyos"          = "HarmonyOS"
    "git"                = "Git"
    "github"             = "GitHub"
    "devops"             = "DevOps"
    "markdown"           = "Markdown"
    "getting-started"    = "Getting Started"
    "english"            = "Tech English"
}

$SourceDir = "c:\Atian\Project\Trae\FANDEX-pj\FANDEX-Web\content"
$TargetDir = "c:\Atian\Project\Trae\FANDEX-pj\FANDEX-App\android\app\src\main\assets\dist-mobile"

Write-Host "=== FANDEX Content Processor v2 ==="

# Clean target docs directory
if (Test-Path "$TargetDir\docs") {
    Remove-Item "$TargetDir\docs" -Recurse -Force
}
New-Item -ItemType Directory -Path "$TargetDir\docs" -Force | Out-Null

$script:modules = @()
$script:documents = @()

# Scan content/{category}/ directories
$categoryDirs = Get-ChildItem -Path $SourceDir -Directory | Sort-Object Name

foreach ($catDir in $categoryDirs) {
    # Each category dir contains module subdirectories
    $moduleDirs = Get-ChildItem -Path $catDir.FullName -Directory | Sort-Object Name

    foreach ($modDir in $moduleDirs) {
        $moduleName = $modDir.Name
        $categoryId = $ModuleCategoryMap[$moduleName]

        if (-not $categoryId) {
            Write-Host "  SKIP: $moduleName (no category mapping)"
            continue
        }

        $mdFiles = @(Get-ChildItem -Path $modDir.FullName -Filter "*.md" -File | Sort-Object Name)
        $mdxFiles = @(Get-ChildItem -Path $modDir.FullName -Filter "*.mdx" -File | Sort-Object Name)
        $allFiles = @($mdFiles) + @($mdxFiles)

        if ($allFiles.Count -eq 0) {
            continue
        }

        Write-Host "  Processing: $moduleName ($($allFiles.Count) files) -> $categoryId"

        # Create target module directory
        $targetModuleDir = "$TargetDir\docs\$moduleName"
        if (-not (Test-Path $targetModuleDir)) {
            New-Item -ItemType Directory -Path $targetModuleDir -Force | Out-Null
        }

        $docSlugs = @()

        foreach ($file in $allFiles) {
            $slug = [System.IO.Path]::GetFileNameWithoutExtension($file.Name)
            $docSlugs += $slug

            # Read file content
            $content = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)

            # Extract frontmatter
            $title = $slug
            $description = ""
            $difficulty = "intermediate"

            if ($content -match '(?s)^---\r?\n(.*?)\r?\n---') {
                $frontmatter = $Matches[1]
                if ($frontmatter -match "title:\s*['""]?(.+?)['""]?\s*$") {
                    $title = $Matches[1].Trim().Trim("'").Trim('"')
                }
                if ($frontmatter -match "description:\s*['""]?(.+?)['""]?\s*$") {
                    $description = $Matches[1].Trim().Trim("'").Trim('"')
                }
                if ($frontmatter -match "difficulty:\s*(\w+)") {
                    $difficulty = $Matches[1]
                }
            }

            # Strip frontmatter, keep body
            $bodyContent = $content -replace '(?s)^---\r?\n.*?\r?\n---\r?\n', ''

            # Write processed .md file
            $targetFile = "$targetModuleDir\$slug.md"
            [System.IO.File]::WriteAllText($targetFile, $bodyContent, [System.Text.Encoding]::UTF8)

            # Add to documents list
            $script:documents += @{
                slug        = $slug
                title       = $title
                module      = $moduleName
                category    = $categoryId
                difficulty  = $difficulty
                description = $description
            }
        }

        # Add module definition
        $moduleLabel = $ModuleLabels[$moduleName]
        if (-not $moduleLabel) {
            $moduleLabel = $moduleName
        }

        $script:modules += @{
            id          = $moduleName
            title       = $moduleLabel
            category    = $categoryId
            description = "$moduleLabel documents"
            documents   = $docSlugs
        }
    }
}

# Generate index.json
$indexData = @{
    version      = "4.0.0"
    generatedAt  = (Get-Date -Format "yyyy-MM-ddTHH:mm:ssZ")
    categories   = $Categories
    modules      = $script:modules
    documents    = $script:documents
}

$indexJson = $indexData | ConvertTo-Json -Depth 10
[System.IO.File]::WriteAllText("$TargetDir\index.json", $indexJson, (New-Object System.Text.UTF8Encoding $true))

Write-Host ""
Write-Host "=== Processing Complete ==="
Write-Host "Total modules: $($script:modules.Count)"
Write-Host "Total documents: $($script:documents.Count)"
Write-Host "Output: $TargetDir"
