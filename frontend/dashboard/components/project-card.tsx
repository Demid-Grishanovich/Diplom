import type { Project } from "@/components/dashboard"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"

interface ProjectCardProps {
  project: Project
}

export function ProjectCard({ project }: ProjectCardProps) {
  const statusConfig = {
    active: { label: "Active", color: "bg-blue-100 text-blue-700" },
    completed: { label: "Completed", color: "bg-green-100 text-green-700" },
  }

  const status = statusConfig[project.status]
  const progressColor = project.progress === 100 ? "bg-green-600" : "bg-primary"

  return (
    <Card className="hover:shadow-lg transition-shadow duration-200 border-border/50 bg-card">
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between gap-2 mb-2">
          <CardTitle className="text-lg font-semibold text-foreground leading-tight">{project.title}</CardTitle>
          <Badge className={`${status.color} border-0 text-xs font-medium whitespace-nowrap`}>{status.label}</Badge>
        </div>
        <CardDescription className="text-sm text-muted-foreground">{project.description}</CardDescription>
      </CardHeader>

      <CardContent className="space-y-4">
        {/* Progress Section */}
        <div>
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-semibold text-foreground">Progress</span>
            <span className="text-sm font-bold text-primary">{project.progress}%</span>
          </div>
          <div className="w-full bg-muted rounded-full h-2.5 overflow-hidden">
            <div
              className={`h-full transition-all duration-500 ${progressColor}`}
              style={{ width: `${project.progress}%` }}
            />
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-2 gap-3 pt-2">
          <div className="bg-muted/50 rounded-md p-3">
            <p className="text-xs text-muted-foreground font-medium">Contributors</p>
            <p className="text-lg font-bold text-foreground mt-1">{project.contributors}</p>
          </div>
          <div className="bg-muted/50 rounded-md p-3">
            <p className="text-xs text-muted-foreground font-medium">Tasks Done</p>
            <p className="text-lg font-bold text-foreground mt-1">{project.tasksCompleted.toLocaleString()}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
