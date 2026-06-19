# FANDEX Content Processor v3
# FANDEX-App 定位：纯代码语法速查工具
# 仅收录编程语言/查询语言/标记语言的语法格式文档
# 扫描 content/{category}/{module}/ 结构，生成 index.json，
# 复制 .md 文件到 Android assets 目录

$ModuleCategoryMap = @{
    "c"                  = "languages"
    "cpp"                = "languages"
    "csharp"             = "languages"
    "go"                 = "languages"
    "java"               = "languages"
    "javascript"         = "languages"
    "kotlin"             = "languages"
    "lua"                = "languages"
    "python"             = "languages"
    "typescript"         = "languages"
    "css"                = "frontend"
    "mysql"              = "database"
    "postgresql"         = "database"
    "redis"              = "database"
    "sql"                = "database"
    "git"                = "tools"
    "markdown"           = "markup"
}

$Categories = @(
    @{ id = "languages"; label = [char]0x7F16 + [char]0x7A0B + [char]0x8BED + [char]0x8A00; color = "#3b82f6" }
    @{ id = "frontend";  label = [char]0x524D + [char]0x7AEF + [char]0x6280 + [char]0x672F; color = "#d63031" }
    @{ id = "database";  label = [char]0x6570 + [char]0x636E + [char]0x5E93;       color = "#00b894" }
    @{ id = "tools";     label = [char]0x5DE5 + [char]0x5177 + [char]0x94FE;       color = "#4f5bd5" }
    @{ id = "markup";    label = [char]0x6807 + [char]0x8BB0 + [char]0x8BED + [char]0x8A00; color = "#e05a2b" }
)

$ModuleLabels = @{
    "c"                  = "C"
    "cpp"                = "C++"
    "csharp"             = "C#"
    "go"                 = "Go"
    "java"               = "Java"
    "javascript"         = "JavaScript"
    "kotlin"             = "Kotlin"
    "lua"                = "Lua"
    "python"             = "Python"
    "typescript"         = "TypeScript"
    "css"                = "CSS"
    "mysql"              = "MySQL"
    "postgresql"         = "PostgreSQL"
    "redis"              = "Redis"
    "sql"                = "SQL"
    "git"                = "Git"
    "markdown"           = "Markdown"
}

$SourceDir = "c:\Atian\Project\Trae\FANDEX-pj\FANDEX-exe\content"
$TargetDir = "c:\Atian\Project\Trae\FANDEX-pj\FANDEX-App\android\app\src\main\assets\dist-mobile"

Write-Host "=== FANDEX Content Processor v3 ==="

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
