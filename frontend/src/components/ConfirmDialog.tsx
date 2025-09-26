import * as React from 'react'
import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from '@mui/material'

export default function ConfirmDialog({ open, title, message, onClose, onConfirm }:{
  open: boolean; title: string; message: string; onClose: ()=>void; onConfirm: ()=>void
}){
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>{message}</DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button onClick={()=>{ onConfirm(); onClose(); }} variant="contained">Confirmar</Button>
      </DialogActions>
    </Dialog>
  )
}

