/* Copy built files to Spring Boot static folder (src/main/resources/static/app) */
const fs = require('fs')
const path = require('path')

const dist = path.resolve(__dirname, '..', 'dist')
const target = path.resolve(__dirname, '..', '..', 'src', 'main', 'resources', 'static', 'app')

function copyDir(src, dest){
  if (!fs.existsSync(dest)) fs.mkdirSync(dest, { recursive: true })
  for (const entry of fs.readdirSync(src)){
    const s = path.join(src, entry)
    const d = path.join(dest, entry)
    const stat = fs.statSync(s)
    if (stat.isDirectory()) copyDir(s, d)
    else fs.copyFileSync(s, d)
  }
}

copyDir(dist, target)
console.log('Copied SPA dist to', target)

