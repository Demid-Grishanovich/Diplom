"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Textarea } from "@/components/ui/textarea"
import { Upload, Bold, Italic, List } from "lucide-react"

export function CreateProjectForm() {
  const [projectTitle, setProjectTitle] = useState("")
  const [instructions, setInstructions] = useState("")
  const [redundancy, setRedundancy] = useState(3)
  const [dragActive, setDragActive] = useState(false)
  const [uploadedFile, setUploadedFile] = useState<string | null>(null)

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(e.type === "dragenter" || e.type === "dragover")
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)

    const files = e.dataTransfer.files
    if (files && files.length > 0) {
      const file = files[0]
      if (file.type === "text/csv" || file.name.endsWith(".csv")) {
        setUploadedFile(file.name)
      }
    }
  }

  const handleFileInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files
    if (files && files.length > 0) {
      setUploadedFile(files[0].name)
    }
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log({
      projectTitle,
      instructions,
      redundancy,
      uploadedFile,
    })
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-8">
      {/* Project Title */}
      <Card className="border-border">
        <CardHeader>
          <CardTitle className="text-lg">Project Details</CardTitle>
          <CardDescription>Give your project a clear, descriptive name</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            <Label htmlFor="project-title" className="text-foreground font-medium">
              Project Title
            </Label>
            <Input
              id="project-title"
              placeholder="e.g., Customer Feedback Classification"
              value={projectTitle}
              onChange={(e) => setProjectTitle(e.target.value)}
              className="bg-background border-input"
              required
            />
          </div>
        </CardContent>
      </Card>

      {/* Instructions */}
      <Card className="border-border">
        <CardHeader>
          <CardTitle className="text-lg">Worker Instructions</CardTitle>
          <CardDescription>Provide clear instructions that workers will follow to complete tasks</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            <div className="flex gap-1 p-2 bg-muted rounded-lg border border-input">
              <button type="button" className="p-2 hover:bg-background rounded transition-colors" title="Bold">
                <Bold size={18} className="text-foreground" />
              </button>
              <button type="button" className="p-2 hover:bg-background rounded transition-colors" title="Italic">
                <Italic size={18} className="text-foreground" />
              </button>
              <button type="button" className="p-2 hover:bg-background rounded transition-colors" title="List">
                <List size={18} className="text-foreground" />
              </button>
            </div>
            <Label htmlFor="instructions" className="text-foreground font-medium">
              Instructions
            </Label>
            <Textarea
              id="instructions"
              placeholder="Enter detailed instructions for workers. Be clear and specific about what needs to be labeled..."
              value={instructions}
              onChange={(e) => setInstructions(e.target.value)}
              className="bg-background border-input min-h-40 resize-none"
              required
            />
          </div>
        </CardContent>
      </Card>

      {/* Upload Dataset */}
      <Card className="border-border">
        <CardHeader>
          <CardTitle className="text-lg">Upload Dataset</CardTitle>
          <CardDescription>Upload a CSV file containing your data to be labeled</CardDescription>
        </CardHeader>
        <CardContent>
          <div
            onDragEnter={handleDrag}
            onDragLeave={handleDrag}
            onDragOver={handleDrag}
            onDrop={handleDrop}
            className={`relative border-2 border-dashed rounded-lg p-8 text-center transition-all ${
              dragActive ? "border-primary bg-primary/5" : "border-border bg-muted/30 hover:bg-muted/50"
            }`}
          >
            <input
              type="file"
              accept=".csv"
              onChange={handleFileInput}
              className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
              required={!uploadedFile}
            />
            <div className="flex flex-col items-center gap-3">
              <div className="p-3 bg-primary/10 rounded-lg">
                <Upload size={24} className="text-primary" />
              </div>
              {uploadedFile ? (
                <>
                  <p className="font-medium text-foreground">{uploadedFile}</p>
                  <p className="text-sm text-muted-foreground">Uploaded successfully</p>
                </>
              ) : (
                <>
                  <p className="font-medium text-foreground">Drag and drop your CSV file here</p>
                  <p className="text-sm text-muted-foreground">or click to browse</p>
                </>
              )}
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Quality Control Settings */}
      <Card className="border-border">
        <CardHeader>
          <CardTitle className="text-lg">Quality Control Settings</CardTitle>
          <CardDescription>Configure redundancy to ensure high-quality labeled data</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            <div>
              <Label htmlFor="redundancy" className="text-foreground font-medium block mb-3">
                Redundancy: <span className="text-primary font-semibold">{redundancy} workers per task</span>
              </Label>
              <div className="flex gap-4 items-center">
                <input
                  type="range"
                  id="redundancy-slider"
                  min="1"
                  max="10"
                  value={redundancy}
                  onChange={(e) => setRedundancy(Number.parseInt(e.target.value))}
                  className="flex-1 h-2 bg-input rounded-lg appearance-none cursor-pointer accent-primary"
                />
                <div className="flex items-center gap-2">
                  <Input
                    id="redundancy"
                    type="number"
                    min="1"
                    max="10"
                    value={redundancy}
                    onChange={(e) => setRedundancy(Math.min(10, Math.max(1, Number.parseInt(e.target.value) || 1)))}
                    className="w-16 text-center bg-background border-input"
                  />
                </div>
              </div>
              <p className="text-xs text-muted-foreground mt-2">
                Higher redundancy ensures better accuracy but increases cost. We recommend 3 workers for most tasks.
              </p>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Action Buttons */}
      <div className="flex gap-3 pt-4">
        <Button
          type="submit"
          className="flex-1 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold py-6"
          disabled={!projectTitle || !instructions || !uploadedFile}
        >
          Launch Project
        </Button>
        <Button type="button" variant="outline" className="px-8 border-border hover:bg-muted bg-transparent">
          Cancel
        </Button>
      </div>
    </form>
  )
}
